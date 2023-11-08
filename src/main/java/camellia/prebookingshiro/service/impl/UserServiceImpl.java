package camellia.prebookingshiro.service.impl;

import camellia.prebookingshiro.domain.Role;
import camellia.prebookingshiro.domain.User;
import camellia.prebookingshiro.mapper.PermissionMapper;
import camellia.prebookingshiro.mapper.RoleMapper;
import camellia.prebookingshiro.mapper.UserMapper;
import camellia.prebookingshiro.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author anthea
 * @date 2023/11/2 12:58
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    public User queryByUserName(String username) {
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        User user = userMapper.selectOne(queryWrapper);
        return user;
    }

    public List<Role> getByUId(Integer uid) {
        return  roleMapper.listByUId(uid);
    }

    public Set<String> getByRId(List<Integer> rids) {
        Set<String> set = new HashSet<>();
        for (Integer rid : rids) {
            List<String> permissions = permissionMapper.listByRId(rid);
            for (String permission : permissions) {
                set.add(permission);
            }
        }
        return set;
    }
}
