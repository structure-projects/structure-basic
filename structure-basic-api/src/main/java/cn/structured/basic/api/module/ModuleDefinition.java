package cn.structured.basic.api.module;

import lombok.Data;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * 模块定义
 *
 * @author chuck
 * @since JDK1.8
 */
@Data
public class ModuleDefinition {

    /**
     * 模块名字
     */
    private String name;

    /**
     * 模块实现类class
     */
    private Class<? extends IModule> moduleClass;

    /**
     * 模块实现对象
     */
    private Object moduleObject;

    /**
     * 模块方法
     */
    private Map<String, Method> methods;

}
