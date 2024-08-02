package cn.structured.basic.api.model;

import lombok.Data;

/**
 * 表现层实体
 *
 * @author chuck
 * @since JDK1.8
 */
@Data
public class ViewEntity extends BasicEntity {

    /**
     * 视图主体内容
     */
    private String body;

}
