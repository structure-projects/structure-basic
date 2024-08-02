package cn.structured.basic.api.model.dao;


import cn.structured.basic.api.model.FieldEntity;
import lombok.Data;

/**
 * 数据模型
 *
 * @author chuck
 * @since JDK1.8
 */
@Data
public class ColumnEntity extends FieldEntity {

    /**
     * 列名
     */
    private String column;

    /**
     * 长度
     */
    private Integer length;

    /**
     * 精度长度
     */
    private Integer mantissa;

}
