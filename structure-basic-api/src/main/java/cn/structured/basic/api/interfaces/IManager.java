package cn.structured.basic.api.interfaces;

/**
 * 资源管理器接口（基本增删改查）
 *
 * @author chuck
 * @since JDK1.8
 */
public interface IManager<T> {

    String save(T t);

    boolean updateById(String id, T t);

    boolean removeById(String id);

    T findById(String id);
}
