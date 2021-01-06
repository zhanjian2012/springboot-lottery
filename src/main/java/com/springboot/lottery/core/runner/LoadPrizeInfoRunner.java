package com.springboot.lottery.core.runner;

import com.springboot.lottery.common.constants.RedisConstants;
import com.springboot.lottery.common.enmus.PrizeTypeEnums;
import com.springboot.lottery.entity.PrizeEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
@Component
public class LoadPrizeInfoRunner implements CommandLineRunner {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Value("${lottery.activity.id:}")
    private String activityId;

    /**
     * 模拟奖品列表
     *
     * @return
     */
    private static List<PrizeEntity> getPrizeList() {
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
            prizeEntity.setSurplusStock(i * 10);
            list.add(prizeEntity);
        }
        return list;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("开始加载奖品信息...");

        // 模拟奖品列表
        List<PrizeEntity> prizeList = getPrizeList();

        // 将奖品放入redis中，使用redis-hash格式
        setPrizeList2RedisHash(prizeList);

        //计算奖品队列
        calcPrizePercentage(prizeList);

        log.info("结束加载奖品信息...");
    }

    /**
     * 设置奖品列表到redisHash中
     */
    private void setPrizeList2RedisHash(List<PrizeEntity> list) {
        for (PrizeEntity pe : list) {
            redisTemplate.opsForHash().put(RedisConstants.ACTIVITY_PRIZE_LIST, pe.getId().toString(), pe);
        }
    }

    /**
     * 计算奖品概率
     */
    private void calcPrizePercentage(List<PrizeEntity> list) {

        List<PrizeEntity> physicalList = new ArrayList<>();
        List<PrizeEntity> virtualList = new ArrayList<>();

        // 实物概率 和 虚拟物概率 和 概率总数
        int physicalPercentage = 0,
                physicalNum = 0,
                virtualPercentage = 0,
                virtualNum = 0,
                totalPercentage = 0;

        // 根据实物和虚拟物类型放入不同队列中
        for (PrizeEntity pe : list) {
            if (pe.getPrizeType() == PrizeTypeEnums.PHYSICAL.getType()) {
                physicalList.add(pe);
                physicalPercentage += pe.getPercentage();
                physicalNum += pe.getSurplusStock();
            } else if (pe.getPrizeType() == PrizeTypeEnums.VIRTUAL.getType()) {
                virtualList.add(pe);
                virtualPercentage += pe.getPercentage();
                virtualNum += pe.getSurplusStock();
            }
            totalPercentage += pe.getPercentage();
        }

        // 计算实物和虚拟物奖品的分布和概率
        List<String> physicalIdList = prizeIdList(physicalList, physicalPercentage, physicalNum);
        log.info("实物{}个，具体={}", physicalIdList.size(), physicalIdList);
        String physicalKey = String.format(RedisConstants.ACTIVITY_PHYSICAL_PRIZE_ID_LIST, activityId);
        redisTemplate.opsForList().rightPushAll(physicalKey, physicalIdList);

        List<String> virtualIdList = prizeIdList(virtualList, virtualPercentage, virtualNum);
        log.info("虚物{}个，具体={}", virtualIdList.size(), virtualIdList);
        String virtualKey = String.format(RedisConstants.ACTIVITY_VIRTUAL_PRIZE_ID_LIST, activityId);
        redisTemplate.opsForList().rightPushAll(virtualKey, virtualIdList);

        // 实物概率
        String physicalPercentageKey = String.format(RedisConstants.ACTIVITY_PRIZE_PHYSICAL_PERCENTAGE, activityId);
        redisTemplate.opsForValue().set(physicalPercentageKey, physicalPercentage);

        // 总概率
        String totalPercentageKey = String.format(RedisConstants.ACTIVITY_PRIZE_TOTAL_PERCENTAGE, activityId);
        redisTemplate.opsForValue().set(totalPercentageKey, totalPercentage);

        //总库存
//        String totalStockKey = String.format(RedisConstants.ACTIVITY_PRIZE_TOTAL_STOCK, activityId);
//        redisTemplate.opsForValue().set(totalStockKey, physicalIdList.size() + virtualIdList.size());


    }

    /**
     * 计算奖品的概率分布
     */
    private List<String> prizeIdList(List<PrizeEntity> list, int percentage, int prizeNum) {
        List<String> ids = new ArrayList<>();
        for (int m = 0; m < prizeNum; m++) {
            int rand = new Random().nextInt(percentage);
            int temp = 0;
            for (PrizeEntity prizeEntity : list) {
                temp += prizeEntity.getPercentage();
                if (rand <= temp) {
                    ids.add(prizeEntity.getId().toString());
                    break;
                }
            }
        }
        return ids;
    }

}
