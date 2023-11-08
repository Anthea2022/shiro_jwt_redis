package camellia.prebookingshiro.config;

import camellia.prebookingshiro.token.JwtToken;
import camellia.prebookingshiro.util.JWTUtil;
import camellia.prebookingshiro.util.RedisUtil;
import io.jsonwebtoken.Claims;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import static camellia.prebookingshiro.util.JWTUtil.REFRESH_KEY;

/**
 * @author anthea
 * @date 2023/11/2 19:18
 */
public class JwtFilter extends BasicHttpAuthenticationFilter {
    @Override
    protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String token = request.getHeader("Authorization");
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        Claims claims = JWTUtil.parseJWT(token);
        return new JwtToken(token, claims.get("psw", String.class));
    }

    // 是否允许访问
    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object mappedValue) {
        if (isLoginAttempt(servletRequest, servletResponse)) {
            AuthenticationToken token = createToken(servletRequest, servletResponse);
            if (ObjectUtils.isEmpty(token)) {
                return false;
            }
            Subject subject = getSubject(servletRequest, servletResponse);
            subject.login(token);
            try {
                return onLoginSuccess(token, subject, servletRequest, servletResponse);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        // 登录行为或不需要建全行为
        return true;
    }

//    @Override
//    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception{
//        return true;
//    }

    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String token = httpServletRequest.getHeader("Authorization");
        return token != null;
    }

    private boolean refreshToken(ServletRequest servletRequest, ServletResponse servletResponse) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String token = request.getHeader("Authorization");
        Claims claims = JWTUtil.parseJWT(token);
        String username = claims.getId();

        Long current = claims.get("currMills", Long.class);
        Long redisCurr = (long) RedisUtil.get(username);
        if (redisCurr.equals(redisCurr)) {
            long currentTimeMillis = System.currentTimeMillis();
            RedisUtil.set(username, currentTimeMillis, REFRESH_KEY);
            String newToken = JWTUtil.createToken(username, claims.get("psw", String.class), "user", currentTimeMillis);
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            response.setHeader("Authorization", newToken);
            return true;
        }
        return false;
    }

    @Override
    protected boolean onLoginSuccess(AuthenticationToken jwtToken, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
        String token = (String) jwtToken.getPrincipal();
        if (token != null) {
            Claims claims = JWTUtil.parseJWT(token);
            if (claims != null) {
                String username = claims.getId();
                Long currMilli = claims.get("currMills", Long.class);
                Long currInRedis = (Long) RedisUtil.get(username);
                if (currMilli.equals(currInRedis)) {
                    return true;
                } else {
                    return refreshToken(request, response);
                }
            }
        }
        return false;
    }
}
