package com.siupay.flow.order.handler;

import javax.annotation.Resource;

import com.siupay.flow.order.PayinCreateOrderContext;
import com.siupay.manager.PdcMsgService;
import com.siupay.utils.BaseFacade;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PayinEventCollector extends BaseFacade implements Command {

    @Resource
    private PdcMsgService pdcMsgService;


    @Override
    public boolean execute(Context context) {
        PayinCreateOrderContext createOrderContext = (PayinCreateOrderContext) context;

        pdcMsgService.collect(createOrderContext.getCreatePayinOrderResponse().getOrderId());
        return true;
    }
}
