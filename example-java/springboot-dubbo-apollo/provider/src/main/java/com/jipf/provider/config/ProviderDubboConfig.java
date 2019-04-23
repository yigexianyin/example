package com.jipf.provider.config;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.ProviderConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.spring.context.annotation.DubboComponentScan;
import com.alibaba.dubbo.rpc.Exporter;
import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author jipengfei
 * springboot dubbo Apollo 整合,采用api方式初始化dubbo(dubbo-spring-boot启动器由于Apollo配置在dubbo之后)
 * ConditionalOnClass注解会检查类加载器中是否存在对应的类，如果有的话被注解修饰的类就有资格被Spring容器所注册，否则会被skip
 * DubboComponentScan dubbo扫描需要注册的包
 */
@Configuration
@ConditionalOnClass(Exporter.class)
@DubboComponentScan(basePackages = "com.jipf.provider")
public class ProviderDubboConfig {

    @ApolloConfig
    private Config config;

    /**
     * @Author: jipf
     * @Description: dubbo.application 应用配置
     */
    @Bean
    public ApplicationConfig applicationConfig() {

        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setName(this.config.getProperty("dubbo.application.name", "spring-boot-dubbo-provider"));
        return applicationConfig;
    }

    /**
     * @Author: jipf
     * @Description: dubbo.registry 注册中心配置
     */
    @Bean
    public RegistryConfig registryConfig() {

        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setAddress(this.config.getProperty("dubbo.registry.address", "zookeeper://127.0.0.1:2181"));
        return registryConfig;
    }

    /**
     * @Author: jipf
     * @Description: dubbo.protocol 协议配置
     */
    @Bean
    public ProtocolConfig protocolConfig() {

        ProtocolConfig protocolConfig = new ProtocolConfig();
        protocolConfig.setName(this.config.getProperty("dubbo.protocol.name", "dubbo"));
        protocolConfig.setPort(this.config.getIntProperty("dubbo.protocol.port", 20880));
        protocolConfig.setHost(this.config.getProperty("dubbo.protocol.host", "127.0.0.1"));
        return protocolConfig;
    }

    /**
     * @Author: jipf
     * @Description: dubbo.provider 提供者配置
     */
    @Bean
    public ProviderConfig providerConfig() {

        ProviderConfig providerConfig = new ProviderConfig();
        providerConfig.setVersion(this.config.getProperty("dubbo.provider.version", "1.0.0"));
        return providerConfig;
    }
}
