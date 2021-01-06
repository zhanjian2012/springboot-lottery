package com.springboot.lottery.common.util;

import com.springboot.lottery.entity.PrizeEntity;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
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


    public static void main(String[] args) {
        String[] arr = {"1","1","1","2","2","2","2","2","3","3","3","3","1"};
        Collections.shuffle(Arrays.asList(arr));
        log.info("{}", arr);
        for(int i=0; i<arr.length; i++) {
            System.out.print(arr[i] + ",");
        }
    }
}
