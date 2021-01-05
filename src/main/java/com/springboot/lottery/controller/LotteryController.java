package com.springboot.lottery.controller;

import com.springboot.lottery.service.LotteryService;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/lottery")
public class LotteryController {

    @Resource
    private LotteryService lotteryService;

    @GetMapping("/win")
    public String win(String userId) {
        if(StringUtils.isEmpty(userId)) {
            return "用户ID不能为空";
        }
        return lotteryService.win(userId);
    }

}
