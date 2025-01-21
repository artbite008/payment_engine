package com.siupay.flow.bind.handler;

import com.siupay.common.api.exception.ErrorCode;
import com.siupay.common.api.exception.PaymentError;
import com.siupay.common.api.exception.PaymentException;
import com.siupay.common.lib.enums.PaymentMethod;
import com.siupay.common.lib.json.JsonUtils;
import com.siupay.constant.Constant;
import com.siupay.core.dto.BindInstrumentRequest;
import com.siupay.core.dto.BindInstrumentResponse;
import com.siupay.core.dto.PaymentInstrumentDto;
import com.siupay.flow.bind.BindInstrumentContext;
import com.siupay.instrument.PaymentInstrumentApi;
import com.siupay.instrument.dto.PaymentInstrumentBO;
import com.siupay.instrument.dto.card.Card;
import com.siupay.instrument.enums.BindStatus;
import com.siupay.utils.BaseFacade;
import com.siupay.utils.BeanCopyUtil;
import com.siupay.utils.sensors.SensorsTrackingUtils;
import io.vavr.control.Either;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Slf4j
public class BindInstrumentSaveAndReturn extends BaseFacade implements Command {

    @Autowired
    private PaymentInstrumentApi paymentInstrumentApi;

    @Autowired
    private SensorsTrackingUtils sensorsTrackingUtils;

    @Override
    public boolean execute(Context context) {

        BindInstrumentContext bindInstrumentContext = (BindInstrumentContext) context;
        if (!isBindSuccess(bindInstrumentContext)) {
            throw new PaymentException(ErrorCode.STATUS_ERROR);
        }
        PaymentInstrumentBO paymentInstrumentBO = buildPaymentInstrumentBO(bindInstrumentContext);
        log.info("bing instrument create request is {}", JsonUtils.toJson(paymentInstrumentBO));
        Either<PaymentError, PaymentInstrumentBO> either = paymentInstrumentApi.createPaymentInstrument(paymentInstrumentBO);
        log.info("bind instrument create response is {}",JsonUtils.toJson(either));
        if (either.isLeft()){
            log.error("bind instrument create error {}",JsonUtils.toJson(either.getLeft()));
            throw either.getLeft().toPaymentException();
        }
        buildBindInstrumentResponse(bindInstrumentContext,either.get());
        return false;
    }

    private PaymentInstrumentBO buildPaymentInstrumentBO(BindInstrumentContext context){
        BindInstrumentRequest request = context.getBindInstrumentRequest();

        PaymentInstrumentBO paymentInstrumentBO = new PaymentInstrumentBO();
        paymentInstrumentBO.setEmail(request.getEmail());
        paymentInstrumentBO.setDefaultInstrument(request.getIsDefault());
        paymentInstrumentBO.setUid(getUserId());
        paymentInstrumentBO.setBindStatus(BindStatus.BINDED);
        //todo 前端对应ChannelType字段，deposit配置里payment_method = channel_id
        paymentInstrumentBO.setPaymentMethod(PaymentMethod.valueOf(request.getPaymentMethod()));
        Card card = request.getPaymentInstrument().getCard();
        BeanCopyUtil.copyPropertiesIgnoreNull(context.getCardInfo(),card);
        if (Objects.nonNull(context.getCardInfo())&&Objects.nonNull(context.getCardInfo().getBilling())){
            com.siupay.instrument.dto.billing.Billing billing = request.getPaymentInstrument().getCard().getBilling();
            BeanCopyUtil.copyPropertiesIgnoreNull(context.getCardInfo().getBilling(),billing);

            if (Objects.nonNull(context.getCardInfo().getBilling().getAddress())){
                com.siupay.instrument.dto.address.Address address = request.getPaymentInstrument().getCard().getBilling().getAddress();
                BeanCopyUtil.copyPropertiesIgnoreNull(context.getCardInfo().getBilling().getAddress(),address);
                billing.setAddress(address);
            }
            card.setBilling(billing);
        }

        paymentInstrumentBO.setCard(card);
        paymentInstrumentBO.setCardPanMaskToken(request.getPaymentInstrument().getCardPanMaskToken());
        paymentInstrumentBO.setChannelTokenId(context.getChannelTokenId());
        return paymentInstrumentBO;
    }

    private void buildBindInstrumentResponse(BindInstrumentContext context, PaymentInstrumentBO paymentInstrumentBO){
        BindInstrumentResponse response = context.getBindInstrumentResponse();
        response.setChannelId(context.getChannelTokenId());
        PaymentInstrumentDto paymentInstrument = new PaymentInstrumentDto();
        BeanUtils.copyProperties(paymentInstrumentBO, paymentInstrument);
        Card card = context.getBindInstrumentRequest().getPaymentInstrument().getCard();
        BeanUtils.copyProperties(context.getCardInfo(),card);
        if (Objects.nonNull(context.getCardInfo())&& Objects.nonNull(context.getCardInfo().getBilling())){
            com.siupay.instrument.dto.billing.Billing billing = context.getBindInstrumentRequest().getPaymentInstrument().getCard().getBilling();
            BeanUtils.copyProperties(context.getCardInfo().getBilling(),billing);

            if (Objects.nonNull(context.getCardInfo().getBilling().getAddress())){
                com.siupay.instrument.dto.address.Address address = new com.siupay.instrument.dto.address.Address();
                BeanUtils.copyProperties(context.getCardInfo().getBilling().getAddress(),address);
                billing.setAddress(address);
            }
            card.setBilling(billing);
        }
        paymentInstrument.setCard(card);
        response.setPaymentInstrument(paymentInstrument);

        response.setChannelId(context.getChannel().getChannelId());

        context.setBindInstrumentResponse(response);
        //上报卡绑定大数据风控结果
        //上报神策
    }


    private boolean isBindSuccess(BindInstrumentContext bindInstrumentContext){
        return !bindInstrumentContext.getBindStatus().equalsIgnoreCase(Constant.FAILED);
    }
}
