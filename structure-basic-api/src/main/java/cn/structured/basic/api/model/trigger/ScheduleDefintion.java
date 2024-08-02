package cn.structured.basic.api.model.trigger;

import lombok.Data;

/**
 * 调度定义
 *
 * @author chuck
 * @since JDK1.8
 */
@Data
public class ScheduleDefintion extends TriggerDefinition {

    /**
     * 调度表达式
     */
    private String cron;
}
