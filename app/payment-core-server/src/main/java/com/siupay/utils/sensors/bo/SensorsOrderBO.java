package com.siupay.utils.sensors.bo;

import com.alibaba.fastjson.annotation.JSONField;
import com.siupay.common.lib.sensors.bo.SensorsBaseBO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Sucre
 * @date 2021年11月08日
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SensorsOrderBO extends SensorsBaseBO {

    /**
     * 订单id
     */
    @JSONField(name = "order_id")
    private String orderId;

    /**
     * 是否成功
     */
    @JSONField(name = "is_success")
    private Boolean isSuccess;

    /**
     * 失败原因
     */
    @JSONField(name = "fail_reason")
    private String failReason;

    /**
     * 出入金类型
     */
    @JSONField(name = "withdraw_deposit_type")
    private String withdrawDepositType;

    /**
     * 出入金方式
     */
    @JSONField(name = "withdraw_deposit_method")
    private String withdrawDepositMethod;

    /**
     * 法币
     */
    @JSONField(name = "legal_currency")
    private String fiatCurrency;

    /**
     * 数字货币
     */
    @JSONField(name = "virtual_currency")
    private String cryptoCurrency;
}
