package cn.structured.basic.api.model;

import lombok.Data;

import java.util.Map;

/**
 * create by chuck 2024/4/16
 *
 * @author chuck
 * @since JDK1.8
 */
@Data
public class RoleEntity extends BasicEntity {

    /**
     * 规则名称
     */
    private String name;

    /**
     * 规则参数
     */
    private Map<String, Object> args;

}
