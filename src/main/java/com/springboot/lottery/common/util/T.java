package com.springboot.lottery.common.util;

import com.springboot.lottery.common.enmus.PrizeTypeEnums;
import com.springboot.lottery.entity.PrizeEntity;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

@Slf4j
public class T {
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


    /**
     * 计算奖品概率
     */
    private static void calcPrizePercentage(List<PrizeEntity> list) {

        List<PrizeEntity> physicalList = new ArrayList<>();
        List<PrizeEntity> virtualList = new ArrayList<>();

        // 实物概率 && 虚拟物概率 && 概率总数
        int physicalPercentage = 0,
                physicalNum = 0,
                virtualPercentage = 0,
                virtualNum = 0;

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
        }

        // 计算实物和虚拟物奖品的分布和概率
        List<String> strings = prizeIdList(physicalList, physicalPercentage, physicalNum);
        log.info("实物{}个，具体={}", strings.size(), strings);
        List<String> strings1 = prizeIdList(virtualList, virtualPercentage, virtualNum);
        log.info("虚物{}个，具体={}", strings1.size(), strings1);

    }

    private static List<String> prizeIdList(List<PrizeEntity> list, int percentage, int prizeNum) {
        List<String> ids = new ArrayList<>();

        for (int m = 0; m < prizeNum; m++) {
            int rand = new Random().nextInt(percentage);
            int temp = 0;
            for (PrizeEntity prizeEntity: list) {
                temp += prizeEntity.getPercentage();
                if (rand < temp) {
                    ids.add(prizeEntity.getId().toString());
                    prizeEntity.setSurplusStock(prizeEntity.getSurplusStock()-1);

                    if(prizeEntity.getSurplusStock() == 0) {
                        list.remove(prizeEntity);
                        percentage = percentage - prizeEntity.getPercentage();
                    }
                    break;
                }
            }
        }
        return ids;
    }

    private static void tt(int percentage, List<String> ids, Map<Integer, String> map) {
        int rand = new Random().nextInt(percentage) + 1;
        if(map.get(rand) != null) {
            ids.add(map.get(rand));
            map.remove(rand);
        } else {
            tt(percentage, ids, map);
        }
    }


    public static void main(String[] args) {
        // 模拟奖品列表
        List<PrizeEntity> prizeList = getPrizeList();
        calcPrizePercentage(prizeList);
    }
}
