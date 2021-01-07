package com.springboot.lottery.core.listener;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class Publisher {

    @Resource
    private RedisTemplate redisTemplate;

    public void sendMessage(String channel, Object message) {
        redisTemplate.convertAndSend(channel, message);
    }
}
