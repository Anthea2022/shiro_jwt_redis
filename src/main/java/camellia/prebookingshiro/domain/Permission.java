package camellia.prebookingshiro.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author anthea
 * @date 2023/11/2 12:16
 */
@TableName("permission")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Permission {
    @TableId
    private Integer id;

    @TableField(value = "permission_name")
    private String permissionName;
}
