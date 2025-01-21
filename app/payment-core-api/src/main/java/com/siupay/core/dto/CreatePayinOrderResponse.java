package com.siupay.core.dto;

import com.siupay.common.api.dto.BaseDto;
import com.siupay.common.api.dto.PaymentAmount;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatePayinOrderResponse extends BaseDto {

    private String orderId;

    private String status;

    private PaymentAmount paymentAmount;

}
