package com.siupay.flow.bind.handler;

import com.siupay.common.api.exception.PaymentError;
import com.siupay.common.lib.json.JsonUtils;
import com.siupay.instrument.PaymentInstrumentValidatorApi;
import com.siupay.instrument.dto.request.PaymentInstrumentBindValidateRequest;
import com.siupay.utils.BaseFacade;
import io.vavr.control.Either;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BindInstrumentValidate extends BaseFacade implements Command {

    @Autowired
    private PaymentInstrumentValidatorApi paymentInstrumentValidatorApi;

    @Override
    public boolean execute(Context context){

        PaymentInstrumentBindValidateRequest request = new PaymentInstrumentBindValidateRequest();
        request.setUid(getUserId());
        log.info("bind instrument validate request is {}", JsonUtils.toJson(request));
        Either<PaymentError, Boolean> either = paymentInstrumentValidatorApi.bindValidate(request);
        log.info("bind instrument validate response is {}",JsonUtils.toJson(either));
        if (either.isLeft()){
            log.error("bind instrument error {}",JsonUtils.toJson(either.getLeft()));
            throw either.getLeft().toPaymentException();
        }
        return false;
    }
}
