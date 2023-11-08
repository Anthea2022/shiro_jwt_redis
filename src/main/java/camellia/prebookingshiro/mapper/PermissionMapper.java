package camellia.prebookingshiro.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author anthea
 * @date 2023/11/2 16:16
 */
@Mapper
public interface PermissionMapper {
    @Select("select permission_name from permission where id in (select pid from role_permission where rid = #{rid})")
    List<String> listByRId(Integer rid);
}
