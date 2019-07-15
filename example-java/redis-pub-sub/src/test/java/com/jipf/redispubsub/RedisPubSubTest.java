package com.jipf.redispubsub;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {RedispubsubApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class RedisPubSubTest {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ChannelTopic topic;

    @Test
    public void testRedisPubSub() {

        redisTemplate.convertAndSend(
                topic.getTopic(),
                "我就测试一下"
        );
    }
}