package com.siupay.flow.bind.handler;

import com.siupay.core.dto.BindInstrumentRequest;
import com.siupay.enums.RiskResultEnum;
import com.siupay.flow.bind.BindInstrumentContext;
import com.siupay.utils.BaseFacade;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RefreshScope
public class BindInstrumentRisk extends BaseFacade implements Command {

    @Value("${payment.risk.enable:true}")
    private boolean riskEnable;

    @Override
    public boolean execute(Context context) throws Exception {
        if (riskEnable){
            BindInstrumentRequest bindInstrumentRequest = ((BindInstrumentContext) context).getBindInstrumentRequest();
            ((BindInstrumentContext) context).setRiskResult(RiskResultEnum.ACCEPT);

            return false;
        }else {
            ((BindInstrumentContext) context).setRiskResult(RiskResultEnum.ACCEPT_3DS);
        }
        return false;
    }
}
