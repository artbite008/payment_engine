package com.siupay.utils.sensors;

import com.siupay.common.lib.enums.ChannelEnum;
import com.siupay.common.lib.enums.TradeType;
import com.siupay.common.lib.sensors.SensorsProperties;
import com.siupay.common.lib.sensors.service.SensorsService;
import com.siupay.core.dal.mybatis.entity.PayinOrderEntity;
import com.siupay.enums.PayinOrderStatusEnum;
import com.siupay.instrument.dto.PaymentInstrumentBO;
import com.siupay.instrument.dto.card.Card;
import com.siupay.instrument.enums.BindStatus;
import com.siupay.instrument.enums.CardStatus;
import com.siupay.utils.sensors.bo.SensorsCardBindingBO;
import com.siupay.utils.sensors.bo.SensorsOrderBO;
import com.siupay.utils.sensors.bo.SensorsThreeDSBO;
import com.siupay.utils.sensors.bo.SensorsThreeDSResultBO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * @author: ywainlan
 * @date: 2022/4/19
 * @Description:
 */
@Slf4j
@Component
public class SensorsTrackingUtils {

    @Qualifier("sensorsServiceImpl")
    @Autowired
    private SensorsService sensorsService;

    @Autowired
    private SensorsProperties sensorsProperties;
    public static final String PLAID_METHOD = "Plaid";
    public static final String BANK_CARD_METHOD = "BANK_CARD";
    public static final String EVENT_NAME_RETURN_ADD_CARD_RESULT = "return_add_card_result";
    public static final String EVENT_NAME_THREEDS_VERIFICATION = "threeDS_verification";
    public static final String EVENT_NAME_THREEDS_RESULT = "threeDS_result";

    /**
     * 订单完成
     *
     * @param payinOrder
     */
    public void sendWhenOrderCompleted(PayinOrderEntity payinOrder) {
        try {
            String status = payinOrder.getStatus();
            if (!PayinOrderStatusEnum.valueOf(status).isFinalState()) {
                return;
            }
            boolean isSuccess = PayinOrderStatusEnum.SUCCEEDED.name().equals(status);
            SensorsOrderBO bo = new SensorsOrderBO();
            bo.setUserId(payinOrder.getUid());
            bo.setOrderId(payinOrder.getPayinOrderId());
            bo.setIsSuccess(isSuccess);
            bo.setFailReason(isSuccess ? null : status);
            transSendorEventTypeAndMethod(payinOrder, bo);

            bo.setFiatCurrency(payinOrder.getDepositFiatCurrency());
            bo.setCryptoCurrency(payinOrder.getDepositCryptoCurrency());
            bo.setEventName(sensorsProperties.getWithdrawDepositEventName());
            sensorsService.send(bo);
        } catch (Exception e) {
            log.error("[SensorsTrackingUtils.sendByPayinOrderEntity] error ", e);
        }
    }

    private void transSendorEventTypeAndMethod(PayinOrderEntity payinOrder, SensorsOrderBO bo) {
        if (TradeType.RECHARGE.name().equalsIgnoreCase(payinOrder.getOrderType())) {
            bo.setWithdrawDepositType(TradeType.DEPOSIT.name().toLowerCase());
        } else if (TradeType.BUY.name().equalsIgnoreCase(payinOrder.getOrderType())) {
            bo.setWithdrawDepositType(TradeType.BUY.name().toLowerCase());
        } else {
            bo.setWithdrawDepositType(payinOrder.getOrderType().toLowerCase());
        }

        ChannelEnum channelEnum = ChannelEnum.get(payinOrder.getChannelId());
        switch (channelEnum) {
            case CHECKOUT_PCI:
                bo.setWithdrawDepositMethod(BANK_CARD_METHOD);
                break;
            case PLAID:
                bo.setWithdrawDepositMethod(PLAID_METHOD);
                break;
            default:
                bo.setWithdrawDepositMethod(channelEnum.name());
                log.warn("withdrawDepositMethod 新增的方式：{}", channelEnum.name());
                break;
        }
    }

    /**
     * 绑卡结果
     * @param paymentInstrumentBO
     */
    public void sendWhenCardBinded(PaymentInstrumentBO paymentInstrumentBO) {
        try {
            SensorsCardBindingBO bo = new SensorsCardBindingBO();
            bo.setIsSuccess(BindStatus.BINDED == paymentInstrumentBO.getBindStatus()
                    && CardStatus.VALID == paymentInstrumentBO.getStatus() ? Boolean.TRUE : Boolean.FALSE);
            bo.setFailReason(bo.getIsSuccess() ? null : paymentInstrumentBO.getStatus().name());
            bo.setUserId(paymentInstrumentBO.getUid());
            Card card = paymentInstrumentBO.getCard();
            if (card != null) {
                bo.setIssuingCountry(card.getIssuerCountry());
                bo.setScheme(card.getScheme());
            }
            bo.setEventName(EVENT_NAME_RETURN_ADD_CARD_RESULT);
            sensorsService.send(bo);
        } catch (Exception e) {
            log.error("[SensorsTrackingUtils.sendByPaymentInstrumentBO] error ", e);
        }
    }

    /**
     * 发生3D时
     * @param payinOrder
     */
    public void sendWhenThreeDS(PayinOrderEntity payinOrder) {
        try {
            SensorsThreeDSBO bo = new SensorsThreeDSBO();
            bo.setOrderId(payinOrder.getPayinOrderId());
            bo.setUserId(payinOrder.getUid());
            bo.setEventName(EVENT_NAME_THREEDS_VERIFICATION);
            sensorsService.send(bo);
        } catch (Exception e) {
            log.error("[SensorsTrackingUtils.sendWhenThreeDS] error ", e);
        }
    }

    /**
     * 收到3D结果时
     * @param payinOrder
     */
    public void sendWhenThreeDSCompleted(PayinOrderEntity payinOrder) {
        try {
            SensorsThreeDSResultBO bo = new SensorsThreeDSResultBO();
            bo.setOrderId(payinOrder.getPayinOrderId());
            bo.setUserId(payinOrder.getUid());
            bo.setIsPassed(payinOrder.getStatus().equalsIgnoreCase(PayinOrderStatusEnum.PAYIN_COMPLETED.name()));
            bo.setEventName(EVENT_NAME_THREEDS_RESULT);
            sensorsService.send(bo);
        } catch (Exception e) {
            log.error("[SensorsTrackingUtils.sendWhenThreeDSCompleted] error ", e);
        }
    }
}
