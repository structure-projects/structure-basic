package cn.structured.basic.core.execute;

import cn.structured.basic.api.model.BasicEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 处理请求
 *
 * @author chuck
 * @since JDK1.8
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class HandleRequest {

    /**
     * 实体定义信息
     */
    protected BasicEntity entity;

    /**
     * 下一个执行节点
     */
    protected HandleRequest nextHandle;

    /**
     * 执行方法
     *
     * @param inputParam 入参
     * @return
     */
    public abstract Object execute(Object inputParam);

}
