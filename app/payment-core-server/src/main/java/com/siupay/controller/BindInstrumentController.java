package com.siupay.controller;


import com.siupay.common.api.exception.PaymentError;
import com.siupay.core.BindInstrumentApi;
import com.siupay.core.dto.BindInstrumentRequest;
import com.siupay.core.dto.BindInstrumentResponse;
import com.siupay.service.BindInstrumentService;
import io.vavr.control.Either;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class BindInstrumentController implements BindInstrumentApi {

    @Autowired
    private BindInstrumentService bindInstrumentService;

    @Override
    public Either<PaymentError, BindInstrumentResponse> bind(@RequestBody @Validated BindInstrumentRequest bindInstrumentRequest) {
        Either<PaymentError,BindInstrumentResponse> either = bindInstrumentService.bind(bindInstrumentRequest);
        return either;
    }
}
