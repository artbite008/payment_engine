package com.siupay.flow.order;

import com.siupay.flow.order.handler.CreatePayinOrderRiskValidate;
import com.siupay.flow.order.handler.FastBuyCurrencyConversion;
import com.siupay.flow.order.handler.PayinEventCollector;
import com.siupay.flow.order.handler.PayinOrderChannelRouter;
import com.siupay.flow.order.handler.PayinOrderFeeCalculate;
import com.siupay.flow.order.handler.PayinOrderSaveAndReturn;
import org.apache.commons.chain.impl.ChainBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class PayinCreateOrderFlow extends ChainBase {

    @Autowired
    private CreatePayinOrderRiskValidate createPayinOrderValidate;
    @Autowired
    private PayinOrderSaveAndReturn payinOrderSaveAndReturn;
    @Autowired
    private PayinOrderChannelRouter payinOrderChannelRouter;
    @Autowired
    private PayinOrderFeeCalculate payinOrderFeeCalculate;
    @Autowired
    private FastBuyCurrencyConversion fastBuyCurrencyConversion;
    @Autowired
    private PayinEventCollector payinEventCollector;

    @PostConstruct
    public void createPayinOrderChain() {
        //下单验证，风控不过下单失败
        this.addCommand(createPayinOrderValidate);
        //渠道路由
        this.addCommand(payinOrderChannelRouter);
        //下单验证校验平台余额
//        this.addCommand(createPayinOrderBalanceValidate);
        //费用计算
        this.addCommand(payinOrderFeeCalculate);
        //快捷买币转化
//        this.addCommand(fastBuyCurrencyConversion);
        //存储下单并返回
        this.addCommand(payinOrderSaveAndReturn);
        //pdc创建订单埋点
//        this.addCommand(payinEventCollector);
    }
}
