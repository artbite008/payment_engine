package com.siupay.core.dal.mybatis.entity;

import lombok.Data;

import javax.smartcardio.Card;

@Data
public class PaymentConfirm {

    private String paymentMethod;

    private String subPaymentMethod;

    private String paymentInstrumentId;

    private Card card;
}
