package cn.structured.basic.api.model.trigger;

import lombok.Data;

/**
 * 消息定义
 *
 * @author chuck
 * @since JDK1.8
 */
@Data
public class MessageDefinition extends TriggerDefinition {

    /**
     * 消息连接ID
     */
    private String messageChannelId;

    /**
     * 订阅主题
     */
    private String topic;

}
