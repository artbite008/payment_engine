package com.siupay.flow.bind;

import com.siupay.flow.bind.handler.*;
import com.siupay.flow.bind.handler.BindInstrumentKYCValidate;
import com.siupay.flow.bind.handler.BindInstrumentRisk;
import com.siupay.flow.bind.handler.BindInstrumentSaveAndReturn;
import com.siupay.flow.bind.handler.BindInstrumentToChannel;
import com.siupay.flow.bind.handler.BindInstrumentValidate;
import org.apache.commons.chain.impl.ChainBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class BindInstrumentFlow extends ChainBase {

    @Autowired
    private BindInstrumentRisk bindInstrumentRisk;
    @Autowired
    private BindInstrumentValidate bindInstrumentValidate;
    @Autowired
    private BindInstrumentToChannel bindInstrumentToChannel;
    @Autowired
    private BindInstrumentSaveAndReturn bindInstrumentSaveAndReturn;
    @Autowired
    private BindInstrumentKYCValidate kycValidate;

    @PostConstruct
    public void bindInstrumentChain (){
        //风控校验
//        this.addCommand(bindInstrumentRisk);
        //KYC校验
//        this.addCommand(kycValidate);
        //验证卡片
//        this.addCommand(bindInstrumentValidate);
        //取渠道绑定卡片
        this.addCommand(bindInstrumentToChannel);
        //保存卡片，并返回
        this.addCommand(bindInstrumentSaveAndReturn);
    }

}
