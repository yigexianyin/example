package com.jipf.redispubsub.sub;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

public class SubscribeListener implements MessageListener {

    /**
     * <h2>消息回调</h2>
     *
     * @param message {@link Message} 消息体 + ChannelName
     * @param pattern 订阅的 pattern, ChannelName 的模式匹配
     */
    @Override
    public void onMessage(Message message, byte[] pattern) {

        String body = new String(message.getBody());
        String channel = new String(message.getChannel());
        String pattern_ = new String(pattern);

        System.out.println(body);
        System.out.println(channel);
        System.out.println(pattern_);       // 如果是 ChannelTopic, 则 channel 字段与 pattern 字段值相同
    }
}