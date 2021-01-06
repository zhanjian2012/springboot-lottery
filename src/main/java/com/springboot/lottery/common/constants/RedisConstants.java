package com.springboot.lottery.common.constants;

/**
 * redis常量
 */
public final class RedisConstants {

    /**
     * 实物奖品ID队列redis-key
     */
    public static final String ACTIVITY_PHYSICAL_PRIZE_ID_LIST = "activity:%s::physicalPrizeIdList";


    /**
     * 虚拟奖品ID队列redis-key
     */
    public static final String ACTIVITY_VIRTUAL_PRIZE_ID_LIST = "activity:%s::virtualPrizeIdList";

    /**
     * 总库存
     */
    @Deprecated
    public static final String ACTIVITY_PRIZE_TOTAL_STOCK = "activity:%s::prizeTotalStock";

    /**
     * 实物概率
     */
    public static final String ACTIVITY_PRIZE_PHYSICAL_PERCENTAGE = "activity:%s::physicalPercentage";
    /**
     * 总概率
     */
    public static final String ACTIVITY_PRIZE_TOTAL_PERCENTAGE = "activity:%s::totalPercentage";


    /**
     * 用户实物中奖
     */
    public static final String ACTIVITY_USER_PHYSICAL_PRIZE = "activity:%s::userId:%s:physical";


    /**
     * 用户抽奖进行中判断
     */
    public static final String ACTIVITY_USERID = "activity:%s::userId::%s";

    /**
     * 活动奖品列表
     */
    public static final String ACTIVITY_PRIZE_LIST = "activity:%s::prizeList";

    /**
     * 活动奖品中奖列表
     */
    public static final String ACTIVITY_PRIZED = "activity:%s::prized::%s";


    /**
     * 活动奖品中奖列表
     */
    public static final String ACTIVITY_PRIZED_TOPIC = "activity:%s::topic";

}
