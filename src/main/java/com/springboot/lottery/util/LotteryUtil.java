package com.springboot.lottery.util;

import com.springboot.lottery.entity.PrizeEntity;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
public class LotteryUtil {

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
