package cn.structured.basic.api.model.api;


import cn.structured.basic.api.model.FieldEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 输出实体
 *
 * @author chuck
 * @since JDK1.8
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ApiOutputFieldEntity extends FieldEntity {

    /**
     * mock的名称
     */
    private String mock;

    /**
     * 自定义示例，如果没有配置mock时优先输出，如果有mock优先输出mock
     */
    private String example;

}
