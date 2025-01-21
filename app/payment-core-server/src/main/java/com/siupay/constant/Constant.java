package com.siupay.constant;

public class Constant {

    public static final String PIR_ = "pir_";

    // deposit recharge 迁移订单前缀
    public static final String PIR_DEPOSIT = "pir_deposit_";

    public static final String BIND_CARD = "BIND_CARD";

    public static final String SUCCEEDED = "SUCCEEDED";

    public static final String FAILED = "FAILED";

    public static final String USDT = "USDT";
    /**
     * apollo支付订单扫描时间窗口key
     */
    public static final String APOLLO_KEY_PAYIN_ORDER_SCAN_TIME_WINDOW = "payin_order_scan_time_window";

    /**
     * apollo 补偿支付成功任务最晚分钟间隔
     */
    public static final String APOLLO_KEY_PAYIN_COMPLETED_START_MINUTES = "payin_order_payin_complete_start_minutes";

    /**
     * apollo 补偿支付成功任务最早分钟间隔
     */
    public static final String APOLLO_KEY_PAYIN_COMPLETED_END_MINUTES = "payin_order_payin_complete_end_minutes";

    // deposit 表时区的超前hours
    public static final int UTC_8_HOURS = 8;
}
