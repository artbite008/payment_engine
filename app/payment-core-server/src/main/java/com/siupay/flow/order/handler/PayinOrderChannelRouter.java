package com.siupay.flow.order.handler;

import com.siupay.common.lib.enums.ChannelEnum;
import com.siupay.core.dto.CreatePayinOrderRequest;
import com.siupay.flow.order.PayinCreateOrderContext;
import com.siupay.instrument.PaymentInstrumentPretreatedApi;
import com.siupay.utils.BaseFacade;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class PayinOrderChannelRouter extends BaseFacade implements Command {

    @Autowired
    private PaymentInstrumentPretreatedApi pretreatedApi;

    @Override
    public boolean execute(Context context) {
        choiceChannel(context);
        return false;
    }

    private void choiceChannel(Context context){
        PayinCreateOrderContext createOrderContext = (PayinCreateOrderContext) context;

//        PaymentInstrumentPretreatedQuery query = new PaymentInstrumentPretreatedQuery();
//        log.info("query by instrument id request is {}", JsonUtils.toJson(query));
//        Either<PaymentError, PaymentInstrumentBO> either = pretreatedApi.queryByInstrumentId(query);
//        log.info("query by instrument id response is {}",JsonUtils.toJson(either));
//        if (either.isLeft()){
//            log.error("query by instrument id error {}",either.getLeft());
//            throw either.getLeft().toPaymentException();
//        }
        createOrderContext.setChannel(Optional.ofNullable(createOrderContext.getCreatePayinOrderRequest())
                .map(CreatePayinOrderRequest::getChannelId)
                .map(ChannelEnum::get)
                .orElse(ChannelEnum.CHECKOUT_PCI));
    }
}
