package com.springboot.lottery.service.impl;

import com.springboot.lottery.entity.PrizeEntity;
import com.springboot.lottery.service.LotteryService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class LotteryServiceImpl implements LotteryService {

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Resource
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${lottery.activity.id:}")
    private String activityId;

    @Override
    public String win(String userId) {

        String activityUserKey = "lottery:activity:userId:" + activityId +":"+ userId;
        Boolean isLotterying = redisTemplate.opsForValue().setIfAbsent(activityUserKey, "1", 10, TimeUnit.SECONDS);
        if (!isLotterying) {
            return "抽奖进行中, 稍后再试";
        }

        String prizeKeyArray = "prizeArray:" + activityId;
        String prizeInfo = redisTemplate.opsForList().leftPop(prizeKeyArray);
        if(!StringUtils.isEmpty(prizeInfo)) {
            String prizeKey = "prizeList:" + activityId;
            String prizeId = prizeInfo.split("-")[0];
            PrizeEntity prizeEntity = (PrizeEntity)redisTemplate.opsForHash().get(prizeKey, prizeId);
            kafkaTemplate.send("prize-consumer", prizeId);
            return "恭喜您获得奖品：" + prizeEntity.getPrizeName();
        } else {
            return "活动太火爆，稍后再试";
        }

        //计算获取哪个奖品
//        PrizeEntity prizeRate = getPrizeRate();
//        if (Objects.isNull(prizeRate)) {
//            return "非常遗憾，未中奖";
//        }
//        String prizeIdKey = "lottery:activity:prizes:" + activityId + ":" + prizeRate.getId();
//        Long decrement = redisTemplate.opsForValue().decrement(prizeIdKey);
//        // 如果获取到锁，则进行抽奖并减库存操作
//        if (decrement > 0) {
//            // 抽奖逻辑
//            kafkaTemplate.send("prize-consumer", prizeRate);
//            return "恭喜您获得奖品：" + prizeRate.getPrizeName();
//        } else {
//            redisTemplate.opsForValue().increment(prizeIdKey);
//            return "活动太火爆，稍后再试";
//        }
    }


    /**
     * 判断获取哪个奖品
     */
    private PrizeEntity getPrizeRate() {
        String prizeListKey = "lottery:activity:prizes:" + activityId;
        List<Object> prizeEntityList = redisTemplate.opsForHash().values(prizeListKey);

        // 计算每个物品在总概率的基础下的概率情况
        double nextDouble = new Random().nextDouble();
        // 中奖率
        int prizeRate = 0;
        for (Object obj : prizeEntityList) {
            PrizeEntity prizeEntity = (PrizeEntity) obj;
            prizeRate += prizeEntity.getPercentage();
            if (nextDouble <= prizeRate) {
                return prizeEntity;
            }
        }
        return null;
    }

}
