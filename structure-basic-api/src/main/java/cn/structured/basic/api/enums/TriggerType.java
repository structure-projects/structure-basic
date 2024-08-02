package cn.structured.basic.api.enums;

/**
 * 事件触发器
 *
 * @author chuck
 * @since JDK1.8
 */
public enum TriggerType {

    //HTTP触发，消息触发,调度触发，流程触发，函数触发,结果触发
    HTTP,
    MQ,
    SCHEDULE,
    CALLBACK,//回调 任意几点都是可以callback

}
