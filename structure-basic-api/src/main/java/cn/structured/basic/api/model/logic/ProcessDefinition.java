package cn.structured.basic.api.model.logic;

import cn.structured.basic.api.enums.TransactionType;
import cn.structured.basic.api.model.BasicEntity;
import lombok.Data;

import java.util.List;

/**
 * 流程定义
 *
 * @author chuck
 * @since JDK1.8
 */
@Data
public class ProcessDefinition extends BasicEntity {

    /**
     * 事务类型
     */
    private TransactionType transactionType;

    /**
     * 所有节点
     */
    private List<NodeDefinition> nodes;

}
