package com.siupay.controller;

import com.siupay.common.api.exception.PaymentError;
import com.siupay.core.PaymentServiceApi;
import com.siupay.core.dto.CreatePayinOrderRequest;
import com.siupay.core.dto.CreatePayinOrderResponse;
import com.siupay.core.dto.PayinOrderConfirmRequest;
import com.siupay.core.dto.PayinOrderConfirmResponse;
import com.siupay.service.PaymentService;
import io.vavr.control.Either;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class PaymentServiceController implements PaymentServiceApi {

    @Autowired
    private PaymentService paymentService;

    @Override
    public Either<PaymentError, CreatePayinOrderResponse> create(@RequestBody @Validated CreatePayinOrderRequest request) {
        Either<PaymentError, CreatePayinOrderResponse> either = paymentService.createPayinOrder(request);
        return either;
    }

    @Override
    public Either<PaymentError, PayinOrderConfirmResponse> confirm(@RequestBody @Validated PayinOrderConfirmRequest request) {
        Either<PaymentError, PayinOrderConfirmResponse> either = paymentService.confirmPayinOrder(request);
        return either;
    }

}
