package com.siupay.service;

import com.siupay.channel.core.bo.PayInOrderHistorySyncEventBo;
import com.siupay.channel.core.dto.record.PayinResultEventBo;
import com.siupay.common.api.exception.PaymentError;
import com.siupay.common.lib.event.IEvent;
import com.siupay.core.common.dto.Paging;
import com.siupay.core.dto.AdminQueryHistoryRequest;
import com.siupay.core.dto.CreatePayinOrderRequest;
import com.siupay.core.dto.CreatePayinOrderResponse;
import com.siupay.core.dto.PayinOrderAdminDto;
import com.siupay.core.dto.PayinOrderConfirmRequest;
import com.siupay.core.dto.PayinOrderConfirmResponse;
import com.siupay.core.dto.PayinOrderDto;
import com.siupay.core.dto.PayinOrderQueryHistoryRequest;
import io.vavr.control.Either;

public interface PaymentService {

    Either<PaymentError, CreatePayinOrderResponse> createPayinOrder(CreatePayinOrderRequest request);

    Either<PaymentError, PayinOrderConfirmResponse> confirmPayinOrder(PayinOrderConfirmRequest request);

    void payinAsyncProcess(IEvent<PayinResultEventBo> msg) throws Exception;

    Either<PaymentError, PayinOrderDto> payinOrderQuery(String orderId);

    Either<PaymentError, Paging<PayinOrderDto>> getPageList(PayinOrderQueryHistoryRequest request);

    Either<PaymentError, Paging<PayinOrderAdminDto>> getAdminPageList(AdminQueryHistoryRequest request);

    void payinOrderHistorySyncProcess(IEvent<PayInOrderHistorySyncEventBo> msg);
}
