package com.siupay.flow.confirm;

import com.siupay.flow.confirm.handler.ConfirmPayinOrderToChannel;
import com.siupay.flow.confirm.handler.ConfirmPayinOrderUpdateAndReturn;
import com.siupay.flow.confirm.handler.ConfirmPayinOrderValidate;
import org.apache.commons.chain.impl.ChainBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class PayinOrderConfirmFlow extends ChainBase {


    @Autowired
    private ConfirmPayinOrderValidate confirmPayinOrderValidate;
    @Autowired
    private ConfirmPayinOrderToChannel payinOrderToChannel;
    @Autowired
    private ConfirmPayinOrderUpdateAndReturn updateAndReturn;

    @PostConstruct
    public void confirmPayinOrder(){
        this.addCommand(confirmPayinOrderValidate);
        this.addCommand(payinOrderToChannel);
        this.addCommand(updateAndReturn);
    }
}
