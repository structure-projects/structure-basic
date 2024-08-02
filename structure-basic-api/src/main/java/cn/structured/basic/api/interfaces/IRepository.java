package cn.structured.basic.api.interfaces;

import java.io.Serializable;

/**
 * 仓库接口
 *
 * @author chuck
 * @since JDK1.8
 */
public interface IRepository<T> {

    /**
     * 保存接口
     *
     * @param t
     * @return
     */
    Serializable save(T t);

    /**
     * 通过ID修改
     *
     * @param id id
     * @param t  data
     * @return
     */
    boolean updateById(Serializable id, T t);

    /**
     * 通过ID删除
     *
     * @param id id
     * @return
     */
    boolean removeById(Serializable id);

    /**
     * 通过ID查询数据
     *
     * @param id
     * @return
     */
    T findById(Serializable id);

}
