package com.siupay.core;

import com.siupay.common.api.exception.PaymentError;
import com.siupay.core.common.dto.Paging;
import com.siupay.core.dto.AdminQueryHistoryRequest;
import com.siupay.core.dto.PayinOrderAdminDto;
import io.vavr.control.Either;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name ="PAYMENT-CORE", qualifier = "AdminPayinOrderQueryApi")
public interface AdminPayinOrderQueryApi {

    @PostMapping("/v1/api/payment-core/payin-order/admin/query/history")
    Either<PaymentError, Paging<PayinOrderAdminDto>> adminQueryHistory(@RequestBody AdminQueryHistoryRequest request);
}
