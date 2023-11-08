package camellia.prebookingshiro.controller;

import camellia.prebookingshiro.token.JwtToken;
import camellia.prebookingshiro.util.JWTUtil;
import camellia.prebookingshiro.util.RedisUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

import static camellia.prebookingshiro.util.JWTUtil.REFRESH_KEY;

/**
 * @author anthea
 * @date 2023/11/2 18:34
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private RedisUtil redisUtil;

    @PostMapping("/login")
    public Map<String, String> login(String username, String password) {
        Subject subject = SecurityUtils.getSubject();
        long currentTimeMillis = System.currentTimeMillis();
        String token = JWTUtil.createToken(username, password, "user", currentTimeMillis);
        JwtToken jwtToken = new JwtToken(token, password);
        Map<String, String> map = new HashMap<>();
        try {
            subject.login(jwtToken);
            redisUtil.set(username,currentTimeMillis, REFRESH_KEY);
            System.out.println(currentTimeMillis);
        } catch (UnknownAccountException e) {
            map.put("errMsg", "账号不存在");
            return map;
        } catch (IncorrectCredentialsException e) {
            map.put("errMsg", "密码错误");
            return map;
        }
        map.put("token", token);
        return map;
    }

    @PostMapping("/logout")
    public String login() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "登出成功";
    }

    @GetMapping("/info")
    public Map<String, String> getInfo() {
        Map<String, String> map = new HashMap<>();
        map.put("username", "www");
        map.put("sex", "女");
        map.put("age", "19");
        return map;
    }

    @PostMapping("/isPermitted")
    public Map<String, Boolean> getPermitted() {
        Subject subject = SecurityUtils.getSubject();
        Map<String, Boolean> map = new HashMap<>();
        boolean save = subject.isPermitted("save");
        map.put("save",save);
        boolean delete = subject.isPermitted("delete");
        map.put("delete",delete);
        boolean select = subject.isPermitted("select");
        map.put("select",select);
        boolean admin = subject.hasRole("admin");
        map.put("admin",admin);
        boolean user = subject.hasRole("user");
        map.put("user",user);
        return map;
    }
}
