package com.siupay.flow.bind.handler;

import com.siupay.channel.core.dto.payin.CardPayinVerifyRequest;
import com.siupay.channel.core.dto.payin.CardPayinVerifyResponse;
import com.siupay.channel.core.model.CardInfo;
import com.siupay.channel.core.payin.CardPayInFacade;
import com.siupay.common.api.dto.PaymentAmount;
import com.siupay.common.api.enums.PaymentSystem;
import com.siupay.common.api.exception.PaymentError;
import com.siupay.common.lib.enums.ChannelEnum;
import com.siupay.common.lib.enums.PayCurrency;
import com.siupay.common.lib.enums.TradeType;
import com.siupay.common.lib.json.JsonUtils;
import com.siupay.core.dto.BindInstrumentRequest;
import com.siupay.core.dto.PaymentInstrumentDto;
import com.siupay.enums.RiskResultEnum;
import com.siupay.flow.bind.BindInstrumentContext;
import com.siupay.utils.BaseFacade;
import io.vavr.control.Either;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

import static com.siupay.constant.Constant.FAILED;

@Component
@Slf4j
public class BindInstrumentToChannel extends BaseFacade implements Command {

    @Autowired
    private CardPayInFacade cardPayInFacade;

    @Override
    public boolean execute(Context context) throws Exception {
        cardVerifyToChannelCore(context);
        return false;
    }

    private void cardVerifyToChannelCore(Context context){
        CardPayinVerifyRequest cardPayinVerifyRequest = new CardPayinVerifyRequest();
        buildCardPayinVerifyRequest(cardPayinVerifyRequest,context);
        log.info("bind payment instrument card verify request is {}", JsonUtils.toJson(cardPayinVerifyRequest));
        Either<PaymentError, CardPayinVerifyResponse> either =  cardPayInFacade.cardVerify(cardPayinVerifyRequest);
        log.info("bind payment instrument card verify response is {}", JsonUtils.toJson(either));
        if (either.isLeft()){
            log.error("bind instrument error {}",JsonUtils.toJson(either.getLeft()));
            throw either.getLeft().toPaymentException();
        }
        ((BindInstrumentContext) context).setChannelTokenId(either.get().getChannelTrackId());
        //todo 渠道选择
        ((BindInstrumentContext) context).setChannel(ChannelEnum.CHECKOUT_PCI);
        ((BindInstrumentContext) context).setBindStatus(either.get().getStatus().toUpperCase());
        ((BindInstrumentContext) context).setCardInfo(either.get().getCardInfo());
        if (either.get().getStatus().equalsIgnoreCase(FAILED)){
            log.error("bind instrument error status {}",either.get().getStatus());
        }
    }

    private void buildCardPayinVerifyRequest(CardPayinVerifyRequest cardPayinCreateRequest,Context context){
        BindInstrumentRequest bindInstrumentRequest = ((BindInstrumentContext) context).getBindInstrumentRequest();

        cardPayinCreateRequest.setRequestId(UUID.randomUUID().toString().replace("-",""));
        PaymentInstrumentDto paymentInstrument = bindInstrumentRequest.getPaymentInstrument();

        PaymentAmount amount = new PaymentAmount();
        amount.setAmount(BigDecimal.ZERO);
        amount.setCurrency(PayCurrency.USD.name());
        cardPayinCreateRequest.setAmount(amount);
        cardPayinCreateRequest.setChannelId(ChannelEnum.CHECKOUT_PCI.getChannelId());
        cardPayinCreateRequest.setRequestSystem(PaymentSystem.PAYMENT_CORE.getSystemId());
//        cardPayinCreateRequest.setNeed3ds(((BindInstrumentContext) context).getRiskResult().equals(RiskResultEnum.ACCEPT_3DS));
        cardPayinCreateRequest.setNeed3ds(false);
        CardInfo cardInfo = new CardInfo();
        BeanUtils.copyProperties(paymentInstrument.getCard(),cardInfo);
        cardInfo.setPciToken(paymentInstrument.getCardPanMaskToken());
        cardPayinCreateRequest.setCardInfo(cardInfo);
        cardPayinCreateRequest.setTradeType(TradeType.CARD_VALIDATE);
        cardPayinCreateRequest.setExternalUserId(getUserId());
    }
}
