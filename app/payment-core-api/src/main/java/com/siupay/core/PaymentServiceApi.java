package com.siupay.core;

import com.siupay.common.api.exception.PaymentError;
import com.siupay.core.dto.CreatePayinOrderRequest;
import com.siupay.core.dto.CreatePayinOrderResponse;
import com.siupay.core.dto.PayinOrderConfirmRequest;
import com.siupay.core.dto.PayinOrderConfirmResponse;
import io.vavr.control.Either;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name ="PAYMENT-CORE", contextId = "PaymentServiceApi")
public interface PaymentServiceApi {

    @PostMapping("/v1/api/payment-core/order/create")
    Either<PaymentError, CreatePayinOrderResponse> create(@RequestBody CreatePayinOrderRequest request);

    @PostMapping("/v1/api/payment-core/order/confirm")
    Either<PaymentError, PayinOrderConfirmResponse> confirm(@RequestBody PayinOrderConfirmRequest request);

}
