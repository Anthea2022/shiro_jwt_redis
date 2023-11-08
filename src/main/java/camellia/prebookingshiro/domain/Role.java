package camellia.prebookingshiro.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author anthea
 * @date 2023/11/2 12:15
 */
@TableName("role")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role {
    @TableId
    private Integer id;

    @TableField(value = "role_name")
    private String roleName;
}
