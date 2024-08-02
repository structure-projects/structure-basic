package cn.structured.basic.core.manager;

import cn.structured.basic.api.model.ValueObject;
import cn.structured.basic.api.model.trigger.TriggerDefinition;
import cn.structured.basic.core.configuration.BasicContext;
import cn.structured.basic.core.execute.HandleRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 执行实现
 *
 * @author chuck
 * @since JDK1.8
 */
@Service
@AllArgsConstructor
public class ExecuteServiceImpl implements IExecuteService {

    private final BasicContext basicContext;

    private final ThreadLocal<String> transaction = new ThreadLocal<>();

    @Override
    public Object execute(ValueObject inputParam) {
        //触发器
        TriggerDefinition triggerDefinition = (TriggerDefinition) inputParam.getEntity();
        //事务ID
        transaction.set(inputParam.getTransactionId());
        HandleRequest handleRequest = basicContext.getHandleRequest(triggerDefinition.getId());
        //执行这个流程
        Object result = handleRequest.execute(inputParam.getValue());
        transaction.remove();
        return result;
    }

}
