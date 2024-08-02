package cn.structured.basic.api.model.dao;

import cn.structured.basic.api.model.BasicEntity;
import lombok.Data;

import java.util.List;

/**
 * 表定义
 *
 * @author chuck
 * @since JDK1.8
 */
@Data
public class TableEntity extends BasicEntity {

    /**
     * 表名
     */
    private String tableName;

    /**
     * 描述
     */
    private String comment;

    /**
     * 列
     */
    private List<ColumnEntity> columns;

    /**
     * 主键
     */
    private String primaryKey;

}
