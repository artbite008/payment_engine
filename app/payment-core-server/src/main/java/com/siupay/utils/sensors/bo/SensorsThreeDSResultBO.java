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
public class SensorsThreeDSResultBO extends SensorsBaseBO {

    /**
     * 订单id
     */
    @JSONField(name = "order_id")
    private String orderId;

    /**
     * 是否通过
     */
    @JSONField(name = "is_passed")
    private Boolean isPassed;

    /**
     * 3ds业务类型
     */
    @JSONField(name = "threeDS_service_type")
    private String bizType = "fiat";
}
