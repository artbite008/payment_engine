package com.siupay.flow.async;

import javax.annotation.PostConstruct;

import com.siupay.flow.async.handler.PayinAsyncNotifyCenter;
import com.siupay.flow.async.handler.PayinAsyncToAccounting;
import com.siupay.flow.async.handler.PayinAsyncValidate;
import org.apache.commons.chain.impl.ChainBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PayinAsyncFlow extends ChainBase {

    @Autowired
    private PayinAsyncValidate payinAsyncValidate;
    @Autowired
    private PayinAsyncNotifyCenter payinAsyncNotifyCenter;
    @Autowired
    private PayinAsyncToAccounting payinAsyncToAccounting;

    @PostConstruct
    public void payinAsyncMsgProcess(){
        //异步消息检查,判断支付状态
        this.addCommand(payinAsyncValidate);
        
        /**
         * payinAsyncToAccounting和payinAsyncToPcc 是二选一执行
         * //购买USDT会走accounting上分
         * //购买非USDT会走PCC撮合交易
         */
        this.addCommand(payinAsyncToAccounting);

        //发送异步通知到notify-center
        this.addCommand(payinAsyncNotifyCenter);
    }
}
