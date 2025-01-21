package com.siupay.controller;

import com.siupay.common.api.exception.PaymentError;
import com.siupay.core.PayinOrderQueryApi;
import com.siupay.core.common.dto.Paging;
import com.siupay.core.dto.PayinOrderDto;
import com.siupay.core.dto.PayinOrderQueryHistoryRequest;
import com.siupay.service.PaymentService;
import io.vavr.control.Either;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class PayinOrderQueryController implements PayinOrderQueryApi {

    @Autowired
    private PaymentService paymentService;

    @Override
    public Either<PaymentError, PayinOrderDto> payinOrderQuery(String orderId) {
        Either<PaymentError, PayinOrderDto> either = paymentService.payinOrderQuery(orderId);
        return either;
    }

    @Override
    public Either<PaymentError, Paging<PayinOrderDto>> payinOrderQueryHistory(@RequestBody @Validated PayinOrderQueryHistoryRequest request) {
        Either<PaymentError, Paging<PayinOrderDto>> either = paymentService.getPageList(request);
        return either;
    }
}
