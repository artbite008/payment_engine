package com.siupay.core;

import com.siupay.common.api.exception.PaymentError;
import com.siupay.core.common.dto.Paging;
import com.siupay.core.dto.PayinOrderDto;
import com.siupay.core.dto.PayinOrderQueryHistoryRequest;
import io.vavr.control.Either;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name ="PAYMENT-CORE", qualifier = "PayinOrderQueryApi")
public interface PayinOrderQueryApi {

    @PostMapping("/v1/api/payment-core/payin-order/query/{orderId}")
    Either<PaymentError, PayinOrderDto> payinOrderQuery(@RequestParam("orderId")String orderId);

    @PostMapping("/v1/api/payment-core/payin-order/query/history")
    Either<PaymentError, Paging<PayinOrderDto>> payinOrderQueryHistory(@RequestBody PayinOrderQueryHistoryRequest request);
}
