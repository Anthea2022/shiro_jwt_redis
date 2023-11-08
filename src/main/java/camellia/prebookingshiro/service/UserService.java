package camellia.prebookingshiro.service;

import camellia.prebookingshiro.domain.Role;
import camellia.prebookingshiro.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Set;

/**
 * @author anthea
 * @date 2023/11/2 12:57
 */
public interface UserService extends IService<User> {
    User queryByUserName(String username);

    List<Role> getByUId(Integer uid);

    Set<String> getByRId(List<Integer> rids);
}
