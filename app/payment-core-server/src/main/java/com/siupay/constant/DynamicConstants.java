package com.siupay.constant;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Data
@Component
@RefreshScope
public class DynamicConstants {

    /**
     * 服务名称
     */
    @Value("${spring.application.name}")
    private String application;

    @Value("${fiat.default.scale:2}")
    private Integer fiatScale;

    @Value("${payment.risk.enable:true}")
    private Boolean riskEnable;

    @Value("${payment.3d.cardBins:424242,543603}")
    private String cardBins;
    /**
     * 信用卡快捷买币成功通知subject
     */
    @Value("${payment.notice.bank.card.trade.success.subject:deposit.notice.bank.card.trade.success}")
    private String bankCardTradeSuccessSubject;

    /**
     * 信用卡快捷买币失败通知subject
     */
    @Value("${payment.notice.bank.card.trade.fail.subject:deposit.notice.bank.card.trade.fail}")
    private String bankCardTradeFailSubject;

    /**
     * 信用卡充值成功通知subject
     */
    @Value("${payment.notice.bank.card.recharge.success.subject:deposit.notice.bank.card.recharge.success}")
    private String bankCardRechargeSuccessSubject;

    /**
     * 信用卡充值失败通知subject
     */
    @Value("${payment.notice.bank.card.recharge.fail.subject:deposit.notice.bank.card.recharge.fail}")
    private String bankCardRechargeFailSubject;
    
    /**
     * pcc 撮合中间稳定币
     */
    @Value("${pcc.order.funds:USDT}")
    private String funds;

    /**
     * data-center
     */
    @Value("${data.center.collector.topic:DATA_CENTER_EVENT_COLLECTOR}")
    private String dataCenterEventTopic;
}
