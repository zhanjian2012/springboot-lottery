package com.springboot.lottery.core.mq;

import com.springboot.lottery.common.constants.RedisConstants;
import com.springboot.lottery.entity.PrizeEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;

@Slf4j
@Component
public class PrizeConsumer {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Value("${lottery.activity.id:}")
    private String activityId;

    @KafkaListener(groupId = "prize-consumer-group", topics = "prize-consumer")
    public void listen(Object prizeId) {
        String prizeKey = String.format(RedisConstants.ACTIVITY_PRIZE_LIST, activityId);
        PrizeEntity prizeEntity = (PrizeEntity) redisTemplate.opsForHash().get(prizeKey, prizeId.toString());
        log.error("抽中奖品：{}", prizeEntity);
        if (Objects.nonNull(prizeEntity)) {
            String prizeInfoKey = String.format(RedisConstants.ACTIVITY_PRIZED, activityId, prizeId);
            redisTemplate.opsForValue().increment(prizeInfoKey);
        }
    }
}
