package cn.structured.basic.core.execute;

import cn.structured.basic.api.model.BasicEntity;

/**
 * 流程执行实现
 *
 * @author chuck
 * @since JDK1.8
 */
public class ProcessExecute extends HandleRequest {

    public ProcessExecute(BasicEntity entity) {
        super();
        this.entity = entity;
    }

    @Override
    public Object execute(Object inputParam) {
        return nextHandle.execute(inputParam);
    }
}
