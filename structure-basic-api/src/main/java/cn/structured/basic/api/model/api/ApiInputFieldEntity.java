package cn.structured.basic.api.model.api;

import cn.structured.basic.api.model.FieldEntity;
import cn.structured.basic.api.model.RoleEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 输入实体
 *
 * @author chuck
 * @since JDK1.8
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ApiInputFieldEntity extends FieldEntity {


    /**
     * 是否必填
     */
    private Boolean required;

    /**
     * 描述
     */
    private String remark;


    /**
     * 自定义规则 ->正则表达式 ，邮箱，手机号，数字（范围，最大值，最小值）字符串 (长度限制) 日期（yyyy-mm-dd）,
     */
    private RoleEntity role;

}
