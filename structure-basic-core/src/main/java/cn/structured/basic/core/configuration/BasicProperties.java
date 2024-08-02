package cn.structured.basic.core.configuration;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 文件配置类
 *
 * @author chuck
 * @since JDK1.8
 */
@Getter
@Setter
@ToString
@Configuration
@ConfigurationProperties(prefix = "structure.basic")
public class BasicProperties {

    /**
     * 更新地址
     */
    private List<String> addressList;

    /**
     * 授权令牌
     */
    private String accessToken = "";

    /**
     * 核心线程数
     */
    private Integer corePoolSize = 100;

    /**
     * 最大线程数
     */
    private Integer maximumPoolSize = 200;

    /**
     * 空闲线程存活时间
     */
    private Long keepAliveTime = 3000L;

    /**
     * 时间单位
     */
    private TimeUnit unit = TimeUnit.SECONDS;

    /**
     * 工作队列大小
     */
    private Integer workQueue = 200;

}
