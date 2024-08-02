package cn.structured.basic.core.execute;

import cn.structure.common.exception.CommonException;
import cn.structured.basic.api.enums.NodeType;
import cn.structured.basic.api.model.BasicEntity;
import cn.structured.basic.api.model.logic.NodeDefinition;
import cn.structured.function.api.IHandler;
import cn.structured.function.api.entity.FunctionDefinition;
import cn.structured.function.api.entity.ParamEntity;
import cn.structured.function.api.entity.ScriptFunction;
import cn.structured.function.api.entity.ScriptParams;
import com.alibaba.fastjson.JSON;

import java.util.List;
import java.util.Map;

/**
 * 节点执行实现
 *
 * @author chuck
 * @since JDK1.8
 */
public class NodeExecute extends HandleRequest {

    public NodeExecute(BasicEntity entity) {
        super();
        this.entity = entity;
    }

    /**
     * 输入验证
     *
     * @param inputParam 入参
     * @return
     */
    private boolean inputCheckout(Object inputParam) {
        try {
            if (entity instanceof NodeDefinition) {
                NodeDefinition nodeDefinition = (NodeDefinition) entity;
                //入参实体
//                InputEntity inputEntity = (InputEntity) nodeDefinition.getInputEntity();
//                //验证入参
//                Map<String, FieldEntity> fields = inputEntity.getFields();
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    /**
     * 输出验证
     *
     * @param output 出参
     * @return
     */
    private boolean outputCheckout(Object output) {
        try {
            if (entity instanceof NodeDefinition) {
                NodeDefinition nodeDefinition = (NodeDefinition) entity;
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    @Override
    public Object execute(Object inputParam) {
        //节点定义
        NodeDefinition nodeDefinition = (NodeDefinition) this.entity;
        if (nodeDefinition.getCheckoutParam()) {
            //验证入参
            boolean boolInput = inputCheckout(inputParam);
            if (!boolInput) {
                //终止业务参数错误
                throw new CommonException("500", "终止业务参数错误");
            }
        }
        //函数
        FunctionDefinition function = nodeDefinition.getFunction();
        //处理器 todo 函数需要处理预加载函数
        IHandler handler = function.getHandler();
        if (null == handler) {
            try {
                Class<?> aClass = Class.forName(function.getName());
                handler = (IHandler) aClass.newInstance();
                function.setHandler(handler);
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
        }
        Object result = null;
        if (nodeDefinition.getNodeType() == NodeType.SCRIPT) {
            ScriptParams scriptParams = executeScript(nodeDefinition, JSON.parseObject(JSON.toJSONString(inputParam)));
            assert handler != null;
            result = handler.handler(scriptParams);
        }

        //验证出参
        boolean boolOutput = outputCheckout(result);
        if (nodeDefinition.getCheckoutParam()) {
            if (boolOutput) {
                //终止业务参数错误
                throw new CommonException("500", "终止业务参数错误");
            }
        }
        if (null != nextHandle) {
            return nextHandle.execute(result);
        }
        return result;
    }

    private ScriptParams executeScript(NodeDefinition nodeDefinition, Map<String, Object> value) {
        //构建参数
        ScriptParams params = new ScriptParams();

        //代码片段
        params.setCode(nodeDefinition.getScriptCode());

        //构建函数执行参数
        ScriptFunction function = new ScriptFunction();
        function.setFunctionName(nodeDefinition.getName());

        //构建入参定义
        List<ParamEntity> inputParams = nodeDefinition.getInput();

        //出参定义
        function.setInputParams(inputParams);
        function.setOutputParams(nodeDefinition.getOutput());
        function.setInputValues(value);

        //赋值函数定义
        params.setFunction(function);
        return params;
    }

}
