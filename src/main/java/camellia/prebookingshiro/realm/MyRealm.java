package camellia.prebookingshiro.realm;

import camellia.prebookingshiro.domain.Role;
import camellia.prebookingshiro.domain.User;
import camellia.prebookingshiro.service.UserService;
import camellia.prebookingshiro.token.JwtToken;
import camellia.prebookingshiro.util.JWTUtil;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import io.jsonwebtoken.Claims;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author anthea
 * @date 2023/11/2 12:24
 */
@Component
public class MyRealm extends AuthorizingRealm {
    @Autowired
    private UserService userService;

    @Override
    //获得自己定义的token
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String username = (String) principalCollection.iterator().next();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        User user = userService.queryByUserName(username);
        List<Role> roles = userService.getByUId(user.getId());
        List<String> roleNames = new ArrayList<>();
        List<Integer> roleIds = new ArrayList<>();
        for (Role role : roles) {
            roleIds.add(role.getId());
            roleNames.add(role.getRoleName());
        }
        Set<String> permissionNames = userService.getByRId(roleIds);
        info.addRoles(roleNames);
        info.addStringPermissions(permissionNames);
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
//        UsernamePasswordToken token = (UsernamePasswordToken)authenticationToken;
//        String username = token.getUsername();
//        String password = String.valueOf(token.getPassword());
//        User user = userService.queryByUserNameAndPassword(username, password);
        JwtToken jwtToken = (JwtToken) authenticationToken;
        String token = (String) jwtToken.getPrincipal();
        Claims claims = JWTUtil.parseJWT(token);
        String username = claims.getId();
        User user = userService.queryByUserName(username);
        if (ObjectUtils.isEmpty(user)) {
            return null;
        }
        return new SimpleAuthenticationInfo(username, user.getPassword(), getName());
    }
}
