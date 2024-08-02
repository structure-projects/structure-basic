package cn.structured.basic.api.model.logic;

import cn.structured.basic.api.enums.NodeType;
import cn.structured.basic.api.model.BasicEntity;
import cn.structured.function.api.entity.FunctionDefinition;
import cn.structured.function.api.entity.ParamEntity;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 节点定义
 *
 * @author chuck
 * @since JDK1.8
 */
@Data
public class NodeDefinition extends BasicEntity {

    /**
     * 节点的类型 开始节点 ， 函数节点， 数据节点，HTTP节点 ，结束节点
     */
    private NodeType nodeType;

    /**
     * 验证参数
     */
    private Boolean checkoutParam;

    /**
     * 每个实体的函数定义
     */
    private FunctionDefinition function;

    /**
     * 输入实体定义
     */
    private List<ParamEntity> input;

    /**
     * 入参映射
     */
    private Map<String, String> inputMapping;

    /**
     * 输出实体定义
     */
    private ParamEntity output;

    /**
     * 出参映射
     */
    private Map<String, String> outputMapping;

    /**
     * 脚本代码
     */
    private String scriptCode;

    /**
     * 环境参数
     */
    private Map<String, String> env;

}
