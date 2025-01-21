package com.siupay.controller;

import com.siupay.common.api.exception.PaymentError;
import com.siupay.core.AdminPayinOrderQueryApi;
import com.siupay.core.common.dto.Paging;
import com.siupay.core.dto.AdminQueryHistoryRequest;
import com.siupay.core.dto.PayinOrderAdminDto;
import com.siupay.service.PaymentService;
import io.vavr.control.Either;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class AdminPayinOrderQueryController implements AdminPayinOrderQueryApi {

    @Autowired
    private PaymentService paymentService;

    @Override
    public Either<PaymentError, Paging<PayinOrderAdminDto>> adminQueryHistory(@RequestBody AdminQueryHistoryRequest request) {
        Either<PaymentError, Paging<PayinOrderAdminDto>> either = paymentService.getAdminPageList(request);
        return either;
    }
}
