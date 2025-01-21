package com.siupay.core.dto;

import com.siupay.common.api.dto.BaseDto;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.Map;

@Getter
@Setter
public class PayinOrderConfirmRequest extends BaseDto {

    @NotEmpty(message = "orderId not empty")
    private String orderId;

    //暂时未用，从下单里拿
    private PayinConfirmDto confirm;

    private String reference;

    private Map<String,Object> extraMap;
}
