package cn.structured.basic.api.model;

import cn.structured.function.api.dataspecs.IDataSpecs;
import lombok.Data;

/**
 * 属性实体
 *
 * @author chuck
 * @since JDK1.8
 */
@Data
public class FieldEntity {

    /**
     * 属性id
     */
    private String id;

    /**
     * key
     */
    private String key;

    /**
     * 属性名
     */
    private String name;

    /**
     * 描述
     */
    private String remark;

    /**
     * 数据定义
     */
    private IDataSpecs dataSpecs;

}
