package com.springboot.lottery.core;

import com.springboot.lottery.entity.PrizeEntity;
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
    private RedisTemplate<String, List<PrizeEntity>> redisTemplate;

    @Value("${lottery.activity.id:}")
    private String activityId;

    @Override
    public void run(String... args) throws Exception {
        log.info("开始加载奖品信息...");
        List<PrizeEntity> prizeList = getPrizeList();
        String prizeListKey = "lottery:activity:prizes:" + activityId;
        Long size = redisTemplate.opsForList().size(prizeListKey);
        if (size == null || size == 0) {
            redisTemplate.opsForList().rightPushAll(prizeListKey, prizeList);
        }
        log.info("结束加载奖品信息...");
    }

    private List<PrizeEntity> getPrizeList() {
        List<PrizeEntity> list = new ArrayList<>();

        for (int i = 1; i <= 6; i++) {
            PrizeEntity prizeEntity = new PrizeEntity();
            prizeEntity.setId(i);
            prizeEntity.setPrizeName("奖品" + i);
            if (i == 3) {
                prizeEntity.setPrizeType(1);
            } else {
                prizeEntity.setPrizeType(2);
            }
            prizeEntity.setSurplusStock(100);
            prizeEntity.setTotalStock(100);
            list.add(prizeEntity);
        }
        return list;
    }

}
