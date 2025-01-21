package com.siupay.core;

import com.siupay.common.api.exception.PaymentError;
import com.siupay.core.dto.BindInstrumentRequest;
import com.siupay.core.dto.BindInstrumentResponse;
import io.vavr.control.Either;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name ="PAYMENT-CORE", qualifier = "BindInstrumentApi")
public interface BindInstrumentApi {


    /**
     * 绑卡接口
     * */
    @PostMapping(value = "/v1/api/payment-core/bind/paymentInstrument")
    Either<PaymentError, BindInstrumentResponse> bind(@RequestBody BindInstrumentRequest bindInstrumentRequest);

}
