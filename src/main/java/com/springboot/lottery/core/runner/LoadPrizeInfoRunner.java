package com.springboot.lottery.core.runner;

import com.springboot.lottery.entity.PrizeEntity;
import com.springboot.lottery.util.LotteryUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class LoadPrizeInfoRunner implements CommandLineRunner {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Value("${lottery.activity.id:}")
    private String activityId;

    @Override
    public void run(String... args) throws Exception {

        log.info("开始加载奖品信息...");
        String prizeKeyArray = "prizeArray:" + activityId;
        List<PrizeEntity> prizeList = getPrizeList();
        List<Object> strings = LotteryUtil.prizeList(prizeList);

        redisTemplate.opsForList().remove(prizeKeyArray, 0, redisTemplate.opsForList().size(prizeKeyArray));
        redisTemplate.opsForList().rightPushAll(prizeKeyArray, strings);

        log.info("结束加载奖品信息...");
    }

    private List<PrizeEntity> getPrizeList() {
        String prizeKey = "prizeList:" + activityId;
        List<PrizeEntity> list = new ArrayList<>();
        for (int i = 1; i <= 6; i++) {
            PrizeEntity prizeEntity = new PrizeEntity();
            prizeEntity.setId(i);
            prizeEntity.setPrizeName("奖品" + i);
            if (i == 3) {
                prizeEntity.setPrizeType(1);
                prizeEntity.setPercentage(i * 10);
            } else {
                prizeEntity.setPrizeType(2);
                prizeEntity.setPercentage(i);
            }
            prizeEntity.setSurplusStock(10);
            prizeEntity.setPrizeTotal(100);
            list.add(prizeEntity);
            redisTemplate.opsForHash().put(prizeKey, prizeEntity.getId().toString(), prizeEntity);
        }
        return list;
    }

}
