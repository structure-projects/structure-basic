package cn.structured.basic.api.model;

import cn.structured.basic.api.enums.IOType;
import cn.structured.basic.api.enums.ModelType;
import lombok.Data;

import java.util.Map;

/**
 * 模型实体对象
 *
 * @author chuck
 * @since JDK1.8
 */
@Data
public class ModelEntity extends BasicEntity {

    /**
     * 输入输出的类型
     */
    private IOType ioType;

    /**
     * 属性
     */
    private Map<String, FieldEntity> fields;

    /**
     * 模型的类型
     */
    private ModelType modelType;
}
