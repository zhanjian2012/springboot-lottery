package com.springboot.lottery.core.listener;

import com.springboot.lottery.common.constants.RedisConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class ChangeConfig {


    @Value("${lottery.activity.id:}")
    private String activityId;

    //让监听器监听关心的话题
    @Bean
    public RedisMessageListenerContainer setRedisMessageListenerContainer(LettuceConnectionFactory lettuceConnectionFactory,
                                                                          Subscriber subscriber) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(lettuceConnectionFactory);
        //话题1
        String topic = String.format(RedisConstants.ACTIVITY_PRIZED_TOPIC, activityId);
        container.addMessageListener(subscriber, new PatternTopic(topic));
        return container;
    }


}
