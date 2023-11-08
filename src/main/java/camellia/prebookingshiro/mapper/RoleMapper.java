package camellia.prebookingshiro.mapper;

import camellia.prebookingshiro.domain.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author anthea
 * @date 2023/11/2 16:15
 */
@Mapper
public interface RoleMapper {
    @Select("select id, role_name from role where id in (select rid from user_role where uid = #{uid})")
    List<Role> listByUId(Integer uid);
}
