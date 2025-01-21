package com.siupay.service;

import com.siupay.instrument.dto.PaymentInstrumentBO;

public interface PaymentInstrumentHelp {

    PaymentInstrumentBO queryPaymentInstrumentById(String paymentInstrumentId);

    String getCardBin(String instrumentId);
}
