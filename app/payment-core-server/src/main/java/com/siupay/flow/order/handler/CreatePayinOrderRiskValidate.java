package com.siupay.flow.order.handler;

import com.siupay.constant.DynamicConstants;
import com.siupay.core.dto.CreatePayinOrderRequest;
import com.siupay.enums.RiskResultEnum;
import com.siupay.flow.order.PayinCreateOrderContext;
import com.siupay.service.PaymentInstrumentHelp;
import com.siupay.utils.BaseFacade;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
public class CreatePayinOrderRiskValidate extends BaseFacade implements Command {

    @Autowired
    private DynamicConstants dynamicConstants;

    @Autowired
    private PaymentInstrumentHelp paymentInstrumentHelp;

    @Override
    public boolean execute(Context context) {
        CreatePayinOrderRequest request = ((PayinCreateOrderContext)context).getCreatePayinOrderRequest();
        String cardBin = paymentInstrumentHelp.getCardBin(request.getConfirm().getPaymentInstrumentId());
        List<String> list = Arrays.asList(dynamicConstants.getCardBins().split(","));

//        if (dynamicConstants.getRiskEnable()){
        if (list.contains(cardBin)){
            ((PayinCreateOrderContext)context).setRiskResult(RiskResultEnum.ACCEPT_3DS);
        }else{
            ((PayinCreateOrderContext)context).setRiskResult(RiskResultEnum.ACCEPT);
        }

        return false;
    }


}
