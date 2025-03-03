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
public class SensorsCardBindingBO extends SensorsBaseBO {

    /**
     * 卡组织
     */
    @JSONField(name = "card_org")
    private String scheme;

    /**
     * 发卡国
     */
    @JSONField(name = "card_country")
    private String issuingCountry;

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
}
