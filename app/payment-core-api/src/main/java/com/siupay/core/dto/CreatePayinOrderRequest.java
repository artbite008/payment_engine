package com.siupay.core.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Map;

import com.siupay.common.api.dto.BaseDto;
import com.siupay.common.api.dto.PaymentAmount;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatePayinOrderRequest extends BaseDto {

    private String merchantId;

    @NotEmpty
//    @In(value = { "DEPOSIT","RECHARGE","BUY"})
    private String orderType;

    @NotNull
    private PaymentAmount paymentAmount;

    @NotNull
    @Valid
    private PayinConfirmDto confirm;

    @NotNull
    private OrderDataDto order;

    @NotNull
    @Valid
    private AdditionalDataDto additionalData;

    private String channelId;

    private Map<String,Object> extraMap;

}
