package camellia.prebookingshiro.mapper;

import camellia.prebookingshiro.domain.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author anthea
 * @date 2023/11/2 12:17
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
