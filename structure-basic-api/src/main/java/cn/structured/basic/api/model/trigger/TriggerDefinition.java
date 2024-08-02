package cn.structured.basic.api.model.trigger;

import cn.structured.basic.api.enums.CallType;
import cn.structured.basic.api.enums.TriggerType;
import cn.structured.basic.api.model.BasicEntity;
import cn.structured.basic.api.model.logic.NodeDefinition;
import cn.structured.basic.api.model.logic.ProcessDefinition;
import lombok.Data;

/**
 * create by chuck 2024/5/12
 *
 * @author chuck
 * @since JDK1.8
 */
@Data
public class TriggerDefinition extends BasicEntity {

    /**
     * 是否为节点类型
     */
    private Boolean nodeType;

    /**
     * 触发类型
     */
    private TriggerType triggerType;

    /**
     * 是否同步  true 为同步 否则为异步
     */
    private CallType sync;

    /**
     * 绑定的流程
     */
    private ProcessDefinition boundProcess;

    /**
     * 绑定节点
     */
    private NodeDefinition boundNode;
}
