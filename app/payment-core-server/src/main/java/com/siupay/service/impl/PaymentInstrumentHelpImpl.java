package com.siupay.service.impl;

import com.siupay.common.api.exception.ErrorCode;
import com.siupay.common.api.exception.PaymentError;
import com.siupay.common.api.exception.PaymentException;
import com.siupay.common.lib.json.JsonUtils;
import com.siupay.core.dto.CreatePayinOrderRequest;
import com.siupay.flow.order.PayinCreateOrderContext;
import com.siupay.instrument.PaymentInstrumentPretreatedApi;
import com.siupay.instrument.dto.PaymentInstrumentBO;
import com.siupay.instrument.dto.request.PaymentInstrumentPretreatedQuery;
import com.siupay.service.PaymentInstrumentHelp;
import io.vavr.control.Either;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.chain.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Slf4j
public class PaymentInstrumentHelpImpl implements PaymentInstrumentHelp {

    @Autowired
    private PaymentInstrumentPretreatedApi paymentInstrumentApi;


    public PaymentInstrumentBO queryPaymentInstrumentById(String paymentInstrumentId){
        if (StringUtils.isEmpty(paymentInstrumentId)){
            throw new PaymentException(ErrorCode.BUSINESS_ERROR);
        }
        PaymentInstrumentPretreatedQuery query = new PaymentInstrumentPretreatedQuery(paymentInstrumentId);
        log.info("query payment-instrument paymentInstrumentId={}",paymentInstrumentId);
        Either<PaymentError, PaymentInstrumentBO> either = paymentInstrumentApi.queryByInstrumentId(query);
        log.info("query payment-instrument by paymentInstrumentId response {}", JsonUtils.toJson(either));
        if (either.isLeft()){
            log.error("query payment-instrument by paymentInstrumentId error {}",JsonUtils.toJson(either.getLeft()));
            throw either.getLeft().toPaymentException();
        }
        return either.get();
    }


    public String getCardBin(String instrumentId){
        PaymentInstrumentBO result = this.queryPaymentInstrumentById(instrumentId);
        return result.getCard().getBin();
    }

}
