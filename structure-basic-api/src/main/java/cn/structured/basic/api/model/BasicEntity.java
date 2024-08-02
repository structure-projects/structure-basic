package cn.structured.basic.api.model;

import cn.structured.basic.api.enums.EntityType;
import lombok.Data;

/**
 * 基本实体类
 *
 * @author chuck
 * @since JDK1.8
 */
@Data
public class BasicEntity {

    /**
     * ID唯一标识
     */
    private String id;

    /**
     * 实体唯一路径
     */
    private String path;

    /**
     * 空间名
     */
    private String spaceName;

    /**
     * 名称
     */
    private String name;

    /**
     * 描述
     */
    private String remark;

    /**
     * 实体类型
     */
    private EntityType entityType;

    /**
     * 实体上下文
     */
    private String context;

}
