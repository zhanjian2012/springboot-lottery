package com.springboot.lottery.util;

import com.springboot.lottery.entity.PrizeEntity;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
public class LotteryUtil {

//    public static void main(String[] args) {
//
//        List<PrizeEntity> data1 = new ArrayList<>();
//        for (int i = 1; i <= 6; i++) {
//
//
//            PrizeEntity prizeEntity = new PrizeEntity();
//            prizeEntity.setId(i);
//            prizeEntity.setPrizeName("奖品" + i);
//            if (i == 3) {
//                prizeEntity.setPrizeType(1);
//                prizeEntity.setPercentage(i * 10);
//            } else {
//                prizeEntity.setPrizeType(2);
//                prizeEntity.setPercentage(i);
//            }
//            prizeEntity.setSurplusStock(10);
//            prizeEntity.setPrizeTotal(100);
//            data1.add(prizeEntity);
//        }
//
//        int max=0;
//        List<String> data = new ArrayList<>();
//        for(PrizeEntity prizeEntity : data1) {
//            for(int i=0; i<prizeEntity.getSurplusStock(); i++) {
//                max += 1;
//                data.add(prizeEntity.getId() + "-" + max);
//            }
//        }
//        log.info("data={}, size={}", data, data.size());
//        Collections.shuffle(data);
//        log.info("data={}, size={}", data, data.size());
//
//    }

    public static List<Object> prizeList(List<PrizeEntity> data1) {
        int max = 0;
        List<Object> data = new ArrayList<>();
        for (PrizeEntity prizeEntity : data1) {
            for (int i = 0; i < prizeEntity.getSurplusStock(); i++) {
                max += 1;
                data.add(prizeEntity.getId() + "-" + max);
            }
        }
        Collections.shuffle(data);
        log.info("data={}, size={}", data, data.size());
        return data;
    }
}
