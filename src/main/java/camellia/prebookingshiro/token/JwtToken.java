package camellia.prebookingshiro.token;

import camellia.prebookingshiro.util.JWTUtil;
import lombok.Data;
import org.apache.shiro.authc.RememberMeAuthenticationToken;
import org.springframework.stereotype.Component;

/**
 * @author anthea
 * @date 2023/11/2 15:20
 */
@Data
@Component
public class JwtToken implements RememberMeAuthenticationToken {
    private String token;
    private char[] password;
    private boolean rememberMe;


    public JwtToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return this.password;
    }

    public JwtToken() {
        this.rememberMe = false;
    }

    public JwtToken(String token, char[] password) {
        this(token, (char[])password, false);
    }

    public JwtToken(String token, String password) {
        this(token, (char[])(password != null ? password.toCharArray() : null), false);
    }


    public JwtToken(String token, char[] password, boolean rememberMe) {
        this.token = token;
        this.password = password;
        this.rememberMe = rememberMe;
    }

    public JwtToken(String username, String password, boolean rememberMe) {
        this(username, password != null ? password.toCharArray() : null, rememberMe);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getClass().getName());
        sb.append(" - ");
        sb.append(JWTUtil.parseJWT(this.token).getId());
        sb.append(", rememberMe=").append(this.rememberMe);
        return sb.toString();
    }
}
