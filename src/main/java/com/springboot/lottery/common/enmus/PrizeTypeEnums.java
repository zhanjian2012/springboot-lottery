package com.springboot.lottery.common.enmus;

import lombok.Getter;

@Getter
public enum PrizeTypeEnums {

    PHYSICAL(1, "实物"),
    VIRTUAL(2, "虚拟");

    private int type;
    private String name;

    PrizeTypeEnums(Integer type, String name) {
        this.type = type;
        this.name = name;
    }
}
