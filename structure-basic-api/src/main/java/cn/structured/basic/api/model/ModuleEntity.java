package cn.structured.basic.api.model;

import cn.structured.function.api.entity.FunctionDefinition;
import lombok.Data;

import java.util.Map;

/**
 * create by chuck 2024/4/24
 *
 * @author chuck
 * @since JDK1.8
 */
@Data
public class ModuleEntity extends BasicEntity {

    /**
     * 模块下有多少个功能
     */
    private Map<String, FunctionDefinition> functions;

}
