package com.siupay.service.impl;

import com.siupay.common.api.enums.PaymentSystem;
import com.siupay.common.api.exception.ErrorCode;
import com.siupay.common.api.exception.PaymentError;
import com.siupay.common.api.exception.PaymentException;
import com.siupay.common.lib.json.JsonUtils;
import com.siupay.core.dto.BindInstrumentRequest;
import com.siupay.core.dto.BindInstrumentResponse;
import com.siupay.flow.bind.BindInstrumentContext;
import com.siupay.flow.bind.BindInstrumentFlow;
import com.siupay.service.BindInstrumentService;
import com.siupay.service.DataCenterService;
import io.vavr.control.Either;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BindInstrumentServiceImpl implements BindInstrumentService {

    @Autowired
    private BindInstrumentFlow bindInstrumentFlow;

    @Autowired
    private DataCenterService dataCenterService;

    @Override
    public Either<PaymentError,BindInstrumentResponse> bind(BindInstrumentRequest bindInstrumentRequest) {
        BindInstrumentContext bindInstrumentContext = new BindInstrumentContext();
        bindInstrumentContext.setBindInstrumentRequest(bindInstrumentRequest);
        bindInstrumentContext.setBindInstrumentResponse(new BindInstrumentResponse());
        try {
//            dataCenterService.sendPreCardBindEvent(bindInstrumentRequest);
            bindInstrumentFlow.execute(bindInstrumentContext);
        }catch (PaymentException paymentException){
            PaymentError paymentError = paymentException.toPaymentError();
            paymentError.setSystemId(PaymentSystem.PAYMENT_CORE.getSystemId());
            log.error("bind instrument error {}", JsonUtils.toJson(paymentError));
            return Either.left(paymentError);
        }catch (Exception e){
            log.error("bind instrument error ",e);
            PaymentError paymentError = new PaymentException(ErrorCode.UNKNOW, PaymentSystem.PAYMENT_CORE).toPaymentError();
            return Either.left(paymentError);
        }
//        dataCenterService.sendCardBindedEvent(bindInstrumentRequest,bindInstrumentContext.getBindInstrumentResponse());
        return Either.right(bindInstrumentContext.getBindInstrumentResponse());
    }
}
