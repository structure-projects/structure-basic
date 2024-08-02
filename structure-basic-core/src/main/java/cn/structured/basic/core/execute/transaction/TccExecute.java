package cn.structured.basic.core.execute.transaction;

import cn.structured.basic.core.execute.HandleRequest;

/**
 * TCC分布式事务的实现
 *
 * @author chuck
 * @since JDK1.8
 */
public class TccExecute extends HandleRequest {

    public TccExecute() {
        super();
    }

    //针对分布式的事务管理器

    @Override
    public Object execute(Object inputParam) {
        return null;
    }
}
