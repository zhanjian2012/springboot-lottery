package com.springboot.lottery.core.listener;

import com.springboot.lottery.common.constants.RedisConstants;
import com.springboot.lottery.entity.PrizeEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;

@Slf4j
@Component
public class Subscriber implements MessageListener {

    @Resource
    private RedisTemplate redisTemplate;

    @Value("${lottery.activity.id:}")
    private String activityId;

    @Override
    public void onMessage(Message message, byte[] pattern) {

        byte[] body = message.getBody();
        String msgBody = (String) redisTemplate.getValueSerializer().deserialize(body);
        System.out.println(msgBody);

        String prizeKey = String.format(RedisConstants.ACTIVITY_PRIZE_LIST, activityId);
        PrizeEntity prizeEntity = (PrizeEntity) redisTemplate.opsForHash().get(prizeKey, msgBody);

        log.error("抽中奖品：{}", prizeEntity);
        if (Objects.nonNull(prizeEntity)) {
            String prizeInfoKey = String.format(RedisConstants.ACTIVITY_PRIZED, activityId, msgBody);
            redisTemplate.opsForValue().increment(prizeInfoKey);
        }
    }
}
