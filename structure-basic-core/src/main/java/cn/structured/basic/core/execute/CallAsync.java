package cn.structured.basic.core.execute;

import cn.hutool.extra.spring.SpringUtil;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 异步执行接口实现
 *
 * @author chuck
 * @since JDK1.8
 */
public class CallAsync extends HandleRequest {

    private ThreadPoolExecutor executeThreadPool = SpringUtil.getBean(ThreadPoolExecutor.class);

    public CallAsync() {
        super();
    }

    @Override
    public Object execute(Object inputParam) {
        executeThreadPool.execute(() -> nextHandle.execute(inputParam));
        return null;
    }
}
