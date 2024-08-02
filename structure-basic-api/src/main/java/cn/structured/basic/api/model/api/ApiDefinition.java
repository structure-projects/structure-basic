package cn.structured.basic.api.model.api;

import cn.structured.basic.api.model.trigger.TriggerDefinition;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

/**
 * api定义
 *
 * @author chuck
 * @since JDK1.8
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ApiDefinition extends TriggerDefinition {

    /**
     * http 方法 POST GET DELETE ==
     */
    private String method;

    /**
     * web请求路径
     */
    private String uri;

    /**
     * 请求上下文 application/Json
     */
    private String contentType;

    /**
     * 请求头
     */
    private Map<String, String> requestHeaders;

    /**
     * 响应上下文 application/Json ;文件流
     */
    private String responseContentType;

    /**
     * 响应头
     */
    private Map<String, String> responseHeaders;

    /**
     * 输入实体
     */
    private ApiInputEntity inputEntity;

    /**
     * 输出实体
     */
    private ApiOutputEntity outputEntity;

}
