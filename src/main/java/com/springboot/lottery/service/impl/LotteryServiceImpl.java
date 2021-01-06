package com.springboot.lottery.service.impl;

import com.springboot.lottery.common.constants.RedisConstants;
import com.springboot.lottery.entity.PrizeEntity;
import com.springboot.lottery.service.LotteryService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class LotteryServiceImpl implements LotteryService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${lottery.activity.id:}")
    private String activityId;

    @Override
    public String win(String userId) throws Exception {

        //校验库存
        validateStock();

        //校验是否正在抽奖，抽奖间隔1秒
        validateLottering(userId);

        // 奖品信息
        Object prizeId = null;

        //虚物列表
        String virtualKey = String.format(RedisConstants.ACTIVITY_VIRTUAL_PRIZE_ID_LIST, activityId);
        // 实物列表
        String physicalKey = String.format(RedisConstants.ACTIVITY_PHYSICAL_PRIZE_ID_LIST, activityId);

        //判断是否中过实物奖
        if (hasPhysicalPrize(userId)) {
            // 已经实物中过奖
            prizeId = redisTemplate.opsForList().leftPop(virtualKey);
        } else {
            // 实物没中奖，进行计算概率，奖品是否是实物，如果是实物，进行实物抽奖，如果实物没有库存，则进行虚物抽奖
            //                                        如果是虚物，进行虚物抽奖
            if (prizeIsPhysical()) {
                prizeId = redisTemplate.opsForList().leftPop(physicalKey);
            }
            if (Objects.isNull(prizeId)) {
                prizeId = redisTemplate.opsForList().leftPop(virtualKey);
            }
        }

        if (Objects.nonNull(prizeId)) {
            String prizeKey = String.format(RedisConstants.ACTIVITY_PRIZE_LIST, activityId);
            PrizeEntity prizeEntity = (PrizeEntity) redisTemplate.opsForHash().get(prizeKey, prizeId.toString());
            kafkaTemplate.send("prize-consumer", prizeId);
            return "恭喜您获得奖品：" + prizeEntity.getPrizeName();
        } else {
            return "活动太火爆，稍后再试";
        }
    }

    /**
     * 判断库存(当虚拟和实物同时为0时）
     */
    private void validateStock() throws Exception {
        String physicalKey = String.format(RedisConstants.ACTIVITY_PHYSICAL_PRIZE_ID_LIST, activityId);
        Long physicalSize = redisTemplate.opsForList().size(physicalKey);

        String virtualKey = String.format(RedisConstants.ACTIVITY_VIRTUAL_PRIZE_ID_LIST, activityId);
        Long virtualSize = redisTemplate.opsForList().size(virtualKey);
        if (physicalSize == 0 && virtualSize == 0) {
            throw new Exception("库存不足");
        }
    }

    /**
     * 是否正在抽奖判断
     */
    private void validateLottering(String userId) throws Exception {
        String activityUserKey = String.format(RedisConstants.ACTIVITY_USERID, activityId, userId);
        // 1秒之内只能抽一次奖
        Boolean isLotterying = redisTemplate.opsForValue().setIfAbsent(activityUserKey, "1", 1, TimeUnit.SECONDS);
        if (!isLotterying) {
            throw new Exception("抽奖进行中, 稍后再试");
        }
    }

    /**
     * 是否已经中过实物奖
     */
    private boolean hasPhysicalPrize(String userId) {
        String activityUserPhysicalKey = String.format(RedisConstants.ACTIVITY_USER_PHYSICAL_PRIZE, activityId, userId);
        // 1秒之内只能抽一次奖
        Object o = redisTemplate.opsForValue().get(activityUserPhysicalKey);
        return Objects.nonNull(o);
    }

    /**
     * 计算奖品的概率分布
     */
    private boolean prizeIsPhysical() {
        // 实物概率
        String physicalPercentageKey = String.format(RedisConstants.ACTIVITY_PRIZE_PHYSICAL_PERCENTAGE, activityId);
        int physicalPercentage = (int) redisTemplate.opsForValue().get(physicalPercentageKey);
        // 总概率
        String totalPercentageKey = String.format(RedisConstants.ACTIVITY_PRIZE_TOTAL_PERCENTAGE, activityId);
        int totalPercentage = (int) redisTemplate.opsForValue().get(totalPercentageKey);
        // 计算每个物品在总概率的基础下的概率情况
        int random = new Random().nextInt(totalPercentage);
        return physicalPercentage >= random;
    }

}
