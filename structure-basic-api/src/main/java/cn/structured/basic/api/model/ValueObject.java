package cn.structured.basic.api.model;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.util.Objects;

/**
 * 值对象
 *
 * @author chuck
 * @since JDK1.8
 */
@Data
public class ValueObject {

    /**
     * 事务唯一标识
     */
    private String transactionId;

    /**
     * 实体定义信息
     */
    private BasicEntity entity;

    /**
     * 操作的数值
     */
    private Object value;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ValueObject that = (ValueObject) o;
        return Objects.equals(transactionId, that.transactionId) &&
                Objects.equals(entity, that.entity) &&
                Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionId, entity, value);
    }

}
