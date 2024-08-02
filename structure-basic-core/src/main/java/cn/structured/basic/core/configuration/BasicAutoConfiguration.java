package cn.structured.basic.core.configuration;

import cn.hutool.extra.spring.SpringUtil;
import cn.structured.basic.api.interfaces.IRepository;
import cn.structured.basic.api.model.BasicEntity;
import cn.structured.basic.core.endpoint.PageEndpoint;
import cn.structured.basic.core.repository.impl.MemoryRepositoryImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 自动装配类
 *
 * @author chuck
 * @since JDK1.8
 */
@Import(SpringUtil.class)
@Configuration
@ComponentScan(basePackages = "cn.structured.basic.core.**")
public class BasicAutoConfiguration {

    @Resource
    private BasicProperties properties;

    @Resource
    private BasicContext basicContext;

    @Bean
    public ThreadPoolExecutor executeThreadPool() {
        return new ThreadPoolExecutor(
                properties.getCorePoolSize(), // 核心线程数
                properties.getMaximumPoolSize(), // 最大线程数
                properties.getKeepAliveTime(), // 空闲线程存活时间
                properties.getUnit(), // 时间单位
                new LinkedBlockingQueue<>(properties.getWorkQueue()), // 工作队列
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy() // 拒绝策略
        );
    }

    /**
     * 内存
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(IRepository.class)
    public IRepository<BasicEntity> repository() {
        //内存存储
        return new MemoryRepositoryImpl();
    }

    @Bean
    public BasicContext basicContext() {
        BasicContext basicContext = new BasicContext();
        basicContext.setRepository(repository());
        return basicContext;
    }

    @PostConstruct
    public void init() {
        //初始化
        List<String> addressList = properties.getAddressList();
        if (null != addressList) {
            addressList.forEach(address -> basicContext.loadResource(address, properties.getAccessToken()));
        }

    }
}
