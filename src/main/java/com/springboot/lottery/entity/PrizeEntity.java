package com.springboot.lottery.entity;

import lombok.Data;

@Data
public class PrizeEntity implements java.io.Serializable{

    private Integer id;
    private String prizeName;

    // 1=实物；2虚拟物
    private Integer prizeType;

    // 总库存
    private Integer prizeTotal;

    // 剩余库存
    private Integer surplusStock;

    //概率
    private Integer percentage;

}
