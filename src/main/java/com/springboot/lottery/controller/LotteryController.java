package com.springboot.lottery.controller;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/lottery")
public class LotteryController {

    @Resource
    private KafkaTemplate<String, Object> kafkaTemplate;

    @GetMapping("/win")
    public String win() {

        return "success";
    }

}
