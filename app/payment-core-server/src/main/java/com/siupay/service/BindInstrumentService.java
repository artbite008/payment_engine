package com.siupay.service;


import com.siupay.common.api.exception.PaymentError;
import com.siupay.core.dto.BindInstrumentRequest;
import com.siupay.core.dto.BindInstrumentResponse;
import io.vavr.control.Either;

public interface BindInstrumentService {

    Either<PaymentError,BindInstrumentResponse> bind(BindInstrumentRequest bindInstrumentRequest);
}
