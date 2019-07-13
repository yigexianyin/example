package com.jipf.example.redis.config;

import com.jipf.example.redis.sub.SubscribeListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

@Configuration
public class RedisConfig {

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Autowired
    public RedisConfig(RedisConnectionFactory redisConnectionFactory) {
        this.redisConnectionFactory = redisConnectionFactory;
    }

    /**
     * <h2>配置消息监听器</h2>
     * */
    @Bean
    public SubscribeListener listener() {

        return new SubscribeListener();
    }

    /**
     * <h2>配置 发布/订阅 的 Topic</h2>
     * */
    @Bean
    public ChannelTopic channelTopic() {
        return new ChannelTopic("city");
    }

    /**
     * <h2>配置 ChannelName 的模式匹配</h2>
     * */
    @Bean
    public PatternTopic patternTopic() {
        return new PatternTopic("/city/*");
    }

    /**
     * <h2>将消息监听器绑定到消息容器</h2>
     * */
    @Bean
    public RedisMessageListenerContainer messageListenerContainer() {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);

        // 可以修改成 patternTopic, 看一看 MessageListener 中监听的数据
        container.addMessageListener(listener(), channelTopic());
        return container;
    }
}