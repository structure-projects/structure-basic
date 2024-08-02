package cn.structured.basic.core.manager;

import cn.structured.basic.api.model.ValueObject;

/**
 * 执行器接口
 *
 * @author chuck
 * @since JDK1.8
 */
public interface IExecuteService {
    /**
     * 执行接口
     *
     * @param inputParam 执行接口
     * @return 出参
     */
    Object execute(ValueObject inputParam);

}
