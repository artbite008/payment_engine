package com.siupay.flow.confirm.handler;

import com.siupay.channel.core.dto.payin.CardPayinCreateRequest;
import com.siupay.channel.core.dto.payin.CardPayinCreateResponse;
import com.siupay.channel.core.model.Address;
import com.siupay.channel.core.model.Billing;
import com.siupay.channel.core.model.CardInfo;
import com.siupay.channel.core.model.CardType;
import com.siupay.channel.core.payin.CardPayInFacade;
import com.siupay.channel.core.wallet.WalletPayInApi;
import com.siupay.channel.core.wallet.request.WalletCreatePayInRequest;
import com.siupay.channel.core.wallet.response.WalletPayInResponse;
import com.siupay.common.api.dto.PaymentAmount;
import com.siupay.common.api.enums.PaymentSystem;
import com.siupay.common.api.exception.ErrorCode;
import com.siupay.common.api.exception.PaymentError;
import com.siupay.common.api.exception.PaymentException;
import com.siupay.common.lib.enums.PaymentMethod;
import com.siupay.common.lib.enums.TradeType;
import com.siupay.common.lib.json.JsonUtils;
import com.siupay.common.lib.utils.BigDecimalUtils;
import com.siupay.constant.DynamicConstants;
import com.siupay.flow.confirm.PayinOrderConfirmContext;
import com.siupay.core.dal.mybatis.entity.AdditionalData;
import com.siupay.core.dal.mybatis.entity.PayinOrderEntity;
import com.siupay.core.dal.mybatis.repositry.PayinOrderRepository;
import com.siupay.core.dto.PayinOrderConfirmRequest;
import com.siupay.instrument.dto.PaymentInstrumentBO;
import com.siupay.service.PaymentInstrumentHelp;
import com.siupay.utils.BaseFacade;
import io.vavr.control.Either;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Objects;
import java.util.Optional;

@Component
@Slf4j
public class ConfirmPayinOrderToChannel extends BaseFacade implements Command {

    @Autowired
    private CardPayInFacade cardPayInFacade;
    @Autowired
    private PayinOrderRepository payinOrderRepository;
    @Autowired
    private PaymentInstrumentHelp paymentInstrumentHelp;
    @Autowired
    private WalletPayInApi walletPayInApi;
    @Autowired
    private DynamicConstants dynamicConstants;

    @Override
    public boolean execute(Context context) throws Exception {
        PayinOrderConfirmContext confirmContext = (PayinOrderConfirmContext) context;
        switch (PaymentMethod.valueOf(confirmContext.getPaymentMethod().toUpperCase())) {
            case BANK_CARD:
                confirmToBankCardChannel(context);
                break;
            case WALLET:
                confirmToWalletChannel(context);
                break;
            default:
                throw new PaymentException(ErrorCode.PARAM_ERROR, "payment method not supported");
        }
        return false;
    }

    private void confirmToWalletChannel(Context context) {
        PayinOrderConfirmContext confirmContext = (PayinOrderConfirmContext) context;
        PayinOrderConfirmRequest request = confirmContext.getRequest();
        PayinOrderEntity payinOrder = payinOrderRepository.getById(request.getOrderId());
        PaymentAmount amount = new PaymentAmount();
        amount.setAmount(BigDecimalUtils.currencyParse(payinOrder.getPayinAmount(),payinOrder.getPayinCurrency()));
        amount.setCurrency(payinOrder.getPayinCurrency());
        WalletCreatePayInRequest walletCreatePayInRequest = WalletCreatePayInRequest.builder()
                .channelId(payinOrder.getChannelId())
                .externalUserId(payinOrder.getUid())
                .paymentAmount(amount)
                .paymentOrderId(payinOrder.getPayinOrderId())
                .paymentUserId(payinOrder.getPaymentUserId())
                .requestSystem(PaymentSystem.PAYMENT_CORE.getSystemId())
                .source(Optional.ofNullable(payinOrder.getAdditionalData()).map(AdditionalData::getClientFrom).orElse("WEB"))
                .reference(request.getReference())
                .extraData(request.getExtraMap())
                .build();
        log.info("[ConfirmPayinOrderToChannel.confirmToWalletChannel] payin order confirm to channel core request is {}", JsonUtils.toJson(walletCreatePayInRequest));
        Either<PaymentError, WalletPayInResponse> either = walletPayInApi.createPayInTransaction(walletCreatePayInRequest);
        if (either.isLeft()){
            log.error("[ConfirmPayinOrderToChannel.confirmToWalletChannel]payin order confirm to channel core error {}", JsonUtils.toJson(either.getLeft()));
            throw either.getLeft().toPaymentException();
        }
        WalletPayInResponse walletCreatePayInResponse = either.get();
        log.info("[ConfirmPayinOrderToChannel.confirmToWalletChannel] payin order confirm to channel core request is {}", JsonUtils.toJson(walletCreatePayInResponse));
        confirmContext.setChannelCoreId(walletCreatePayInResponse.getPayInTransactionId());
        confirmContext.setChannelCoreStatus(walletCreatePayInResponse.getStatus());
        confirmContext.setApmInfo(walletCreatePayInResponse.getApmInfo());
    }

    private void confirmToBankCardChannel(Context context){
        PayinOrderConfirmContext confirmContext = (PayinOrderConfirmContext) context;
        PayinOrderConfirmRequest request = confirmContext.getRequest();
        PayinOrderEntity payinOrder = payinOrderRepository.getById(request.getOrderId());
        CardPayinCreateRequest createRequest = new CardPayinCreateRequest();
        buildChannelCoreRequest(createRequest, request, payinOrder);
        log.info("payin order confirm to channel core request is {}", JsonUtils.toJson(createRequest));
        Either<PaymentError, CardPayinCreateResponse> either = cardPayInFacade.create(createRequest);
        log.info("payin order confirm to channel core response is {}", JsonUtils.toJson(either));
        if (either.isLeft()) {
            log.error("payin order confirm to channel core error {}", JsonUtils.toJson(either.getLeft()));
            throw either.getLeft().toPaymentException();
        }
        CardPayinCreateResponse createResponse = either.get();
        confirmContext.setChannelCoreId(createResponse.getPayinTrxnId());
        confirmContext.setChannelCoreStatus(createResponse.getStatus());
        confirmContext.setRedirectUrl(createResponse.getRedirectUrl());
    }

    private void buildChannelCoreRequest(CardPayinCreateRequest createRequest, PayinOrderConfirmRequest request,
            PayinOrderEntity payinOrder) {
        createRequest.setExternalUserId(getUserId());
        createRequest.setTradeType(TradeType.valueOf(payinOrder.getOrderType()));
        createRequest.setChannelId(payinOrder.getChannelId());
        PaymentAmount amount = new PaymentAmount();
        amount.setAmount(BigDecimalUtils.currencyParse(payinOrder.getPayinAmount(), payinOrder.getPayinCurrency()));
        amount.setCurrency(payinOrder.getPayinCurrency());
        createRequest.setAmount(amount);
        createRequest.setMerchantId(payinOrder.getMerchantId());
        createRequest.setRequestId(payinOrder.getPayinOrderId());
        CardInfo cardInfo = new CardInfo();
        buildCardInfo(cardInfo, payinOrder);
        createRequest.setCardInfo(cardInfo);
        createRequest.setRequestSystem(PaymentSystem.PAYMENT_CORE.getSystemId());
        createRequest.setNeed3ds(payinOrder.getRiskData().getNeed3DS());
        if (StringUtils.equalsIgnoreCase(payinOrder.getOrderData().getCryptoCurrency(), dynamicConstants.getFunds())
                || payinOrder.getOrderType().equalsIgnoreCase(TradeType.RECHARGE.name())) {// 走accounting上分
            createRequest.setAutoCapture(true);
        } else {// 走PCC撮合交易
            createRequest.setAutoCapture(false);
        }

    }

    private void buildCardInfo(CardInfo cardInfo, PayinOrderEntity payinOrder) {
        PaymentInstrumentBO paymentInstrumentBO = paymentInstrumentHelp
                .queryPaymentInstrumentById(payinOrder.getPaymentConfirm().getPaymentInstrumentId());
        BeanUtils.copyProperties(paymentInstrumentBO.getCard(), cardInfo);
        cardInfo.setCardType(CardType.valueOf(paymentInstrumentBO.getCard().getCardType().name()));
        cardInfo.setPciToken(paymentInstrumentBO.getCardPanMaskToken());
        cardInfo.setChannelToken(paymentInstrumentBO.getChannelTokenId());
        cardInfo.setIsPci(paymentInstrumentBO.getPci());

        if (Objects.nonNull(paymentInstrumentBO.getCard())
                && Objects.nonNull(paymentInstrumentBO.getCard().getBilling())) {
            Billing billing = new Billing();
            BeanUtils.copyProperties(paymentInstrumentBO.getCard().getBilling(), billing);

            if (Objects.nonNull(paymentInstrumentBO.getCard().getBilling().getAddress())) {
                Address address = new Address();
                BeanUtils.copyProperties(paymentInstrumentBO.getCard().getBilling().getAddress(), address);
                billing.setAddress(address);
            }
            cardInfo.setBilling(billing);
        }

        checkIfNoPciCardIsExpired(cardInfo);
    }

    /**
     * 检查none pci card是否过期
     *
     * @param cardInfo
     */
    public void checkIfNoPciCardIsExpired(CardInfo cardInfo) {
        if (cardInfo.getIsPci()) {
            return;
        }

        String expiryMoth = cardInfo.getExpireMonth();
        String expiryYear = cardInfo.getExpireYear();
        if (StringUtils.isBlank(expiryMoth) || StringUtils.isBlank(expiryYear)) {
            log.error("none pci token [{}] card expire info is empty, expiryYear {},expiryMoth{}",
                    cardInfo.getChannelToken(), expiryYear, expiryMoth);
            return;
        }

        Calendar now = Calendar.getInstance();
        now.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH), 23, 59, 59);

        Calendar expire = Calendar.getInstance();
        // 信用卡过期时间为月底,默认28日为过期日期,不考虑大小月份
        expire.set(Integer.valueOf(expiryYear) + 2000, Integer.valueOf(expiryMoth) - 1, 28);
        // 信用卡过期日期往前推1日作为 capture 的时间提前量
        expire.add(Calendar.DAY_OF_MONTH, -1);
        if (now.after(expire)) {
            log.error("token {} with card is expired, expiryYear {},expiryMoth{}", cardInfo.getChannelToken(),
                    expiryYear, expiryMoth);
            throw new PaymentException(ErrorCode.VALIDATE_ERROR, "Your card is expired");
        }
    }
}
