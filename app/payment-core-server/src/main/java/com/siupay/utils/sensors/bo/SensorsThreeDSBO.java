package com.siupay.utils.sensors.bo;

import com.alibaba.fastjson.annotation.JSONField;
import com.siupay.common.lib.sensors.bo.SensorsBaseBO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 
 * @date 2021年11月08日
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SensorsThreeDSBO extends SensorsBaseBO {

    /**
     * 订单id
     */
    @JSONField(name = "order_id")
    private String orderId;
}
