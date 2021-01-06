//package com.springboot.lottery.common.util;
//
//import com.springboot.lottery.common.enmus.PrizeTypeEnums;
//import com.springboot.lottery.entity.PrizeEntity;
//import lombok.extern.slf4j.Slf4j;
//
//import java.util.*;
//
//@Slf4j
//public class T {
//    /**
//     * 模拟奖品列表
//     *
//     * @return
//     */
//    private static List<PrizeEntity> getPrizeList() {
//        List<PrizeEntity> list = new ArrayList<>();
//        for (int i = 1; i <= 6; i++) {
//            PrizeEntity prizeEntity = new PrizeEntity();
//            prizeEntity.setId(i);
//            prizeEntity.setPrizeName("奖品" + i);
//            if (i == 3 || i == 6) {
//                prizeEntity.setPrizeType(1);
//                prizeEntity.setPercentage(i * 10);
//            } else {
//                prizeEntity.setPrizeType(2);
//                prizeEntity.setPercentage(i);
//            }
//            prizeEntity.setSurplusStock(i * 10);
//            list.add(prizeEntity);
//        }
//        return list;
//    }
//
//
//    /**
//     * 计算奖品概率
//     */
//    private static void calcPrizePercentage(List<PrizeEntity> list) {
//
//        List<PrizeEntity> physicalList = new ArrayList<>();
//        List<PrizeEntity> virtualList = new ArrayList<>();
//
//        // 实物概率 && 虚拟物概率 && 概率总数
//        int physicalPercentage = 0,
//                physicalNum = 0,
//                virtualPercentage = 0,
//                virtualNum = 0;
//
//        // 根据实物和虚拟物类型放入不同队列中
//        for (PrizeEntity pe : list) {
//            if (pe.getPrizeType() == PrizeTypeEnums.PHYSICAL.getType()) {
//                physicalList.add(pe);
//                physicalPercentage += pe.getPercentage();
//                physicalNum += pe.getSurplusStock();
//            } else if (pe.getPrizeType() == PrizeTypeEnums.VIRTUAL.getType()) {
//                virtualList.add(pe);
//                virtualPercentage += pe.getPercentage();
//                virtualNum += pe.getSurplusStock();
//            }
//        }
//
//        // 计算实物和虚拟物奖品的分布和概率
//        List<String> strings = prizeIdList(physicalList, physicalPercentage, physicalNum);
//        log.info("实物{}个，具体={}", strings.size(), strings);
//        List<String> strings1 = prizeIdList(virtualList, virtualPercentage, virtualNum);
//        log.info("虚物{}个，具体={}", strings1.size(), strings1);
//
//    }
//
//    private static List<String> prizeIdList(List<PrizeEntity> list, int percentage, int prizeNum) {
//        List<String> ids = new ArrayList<>();
//
//        // key=奖品ID，value=奖品数量, 例如：1=10；3=14
////        Map<String, Integer> prizeNumMap = new LinkedHashMap<>();
////
////        for (PrizeEntity prizeEntity : list) {
////            String key = prizeEntity.getId().toString();
////            prizeNumMap.put(key, prizeEntity.getSurplusStock());
////        }
//
//        for (int m = 0; m < prizeNum; m++) {
//            int rand = new Random().nextInt(percentage);
//            int temp = 0;
//            for (PrizeEntity prizeEntity : list) {
//                temp += prizeEntity.getPercentage();
//                if (rand <= temp) {
////                    String key = prizeEntity.getId().toString();
//                    ids.add(prizeEntity.getId().toString());
////                    prizeNumMap.put(key, prizeNumMap.get(key) - 1);
//                    break;
//                }
//            }
//        }
//        return ids;
//    }
//
//
//    public static void main(String[] args) {
//        // 模拟奖品列表
//        List<PrizeEntity> prizeList = getPrizeList();
//        calcPrizePercentage(prizeList);
//    }
//}
