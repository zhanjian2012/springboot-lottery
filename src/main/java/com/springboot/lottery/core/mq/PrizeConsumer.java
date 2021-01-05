package com.springboot.lottery.core.mq;

import com.springboot.lottery.entity.PrizeEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
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
    public void listen(String prizeId) {
        String prizeKey = "prizeList:" + activityId;
        PrizeEntity prizeEntity = (PrizeEntity)redisTemplate.opsForHash().get(prizeKey, prizeId);
        log.error("抽中奖品：{}", prizeEntity);

        if(Objects.nonNull(prizeEntity)) {
            prizeEntity.setSurplusStock(prizeEntity.getSurplusStock() - 1);
            redisTemplate.opsForHash().put(prizeKey, prizeEntity.getId().toString(), prizeEntity);
            log.info("id={}, total={}", prizeId, redisTemplate.opsForValue().get(prizeId));
            redisTemplate.opsForValue().increment(prizeId);
        }
    }
}
