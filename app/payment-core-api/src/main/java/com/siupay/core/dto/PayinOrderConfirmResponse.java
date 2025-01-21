package com.siupay.core.dto;

import com.siupay.common.api.dto.BaseDto;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class PayinOrderConfirmResponse extends BaseDto {

    private String orderId;

    private String status;

    //暂时用map，后面修改为class
    private NextAction nextAction;

    @Data
    public static class NextAction{
        /**
         * 跳转链接
         */
        private String redirectUrl;
        /**
         * 渠道返回的SDK Token
         */
        private Map<String,Object> apmInfo;
    }
}
