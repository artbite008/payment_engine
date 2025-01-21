package com.siupay.core.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class PayinConfirmDto {

    @NotEmpty(message = "paymentMethod not empty")
//    @In(value = {"BAN_CARD","WALLET"})
    private String paymentMethod;

    private String subPaymentMethod;

    private String paymentInstrumentId;
}
