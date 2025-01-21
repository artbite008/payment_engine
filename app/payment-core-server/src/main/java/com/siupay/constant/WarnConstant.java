package com.siupay.constant;

/**
 * @program: Uther.chen
 * @description: 报警用到的字符信息常量
 * @create: 2022/4/19 17:59
 **/
public class WarnConstant {
    /**
     * 平台数字货币账户余额低于阀值告警title
     */
    public static final String PLATFORM_FAIT_BALANCE_LESS_THAN_LIMIT_TITLE = "平台数字货币账户余额低于阀值";

    /**
     * 平台数字货币账户余额低于阀值告警title
     */
    public static final String PLATFORM_FIAT_CURRENCY_BALANCE_LESS_THAN_LIMIT_TITLE = "平台账户[{0}]余额不足";

    /**
     * 平台数字货币账户余额低于阀值告警content
     */
    public static final String PLATFORM_FAIT_BALANCE_LESS_THAN_LIMIT_CONTENT = "币种[{0}],阀值[{1}],当前余额[{2}]";

}
