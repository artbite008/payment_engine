package com.siupay.flow.order.handler;

import com.siupay.common.lib.enums.TradeType;
import com.siupay.common.lib.idgenerator.IDGeneratorService;
import com.siupay.constant.DynamicConstants;
import com.siupay.core.dal.mybatis.repositry.PayinOrderRepository;
import com.siupay.core.dto.CreatePayinOrderRequest;
import com.siupay.flow.order.PayinCreateOrderContext;
import com.siupay.utils.BaseFacade;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RefreshScope
public class FastBuyCurrencyConversion extends BaseFacade implements Command {

    @Autowired
    private PayinOrderRepository payinOrderRepository;
    @Autowired
    private IDGeneratorService idService;
    @Autowired
    private DynamicConstants dynamicConstants;

    @Value("${crypto.default.scale:6}")
    private Integer cryptoScale;
    @Value("${crypto.platform.usdt.balance:100000}")
    private Long cryptoPlatformUsdtBalance;

    @Override
    public boolean execute(Context context) throws Exception {
        //非快捷买币不用进行法币-虚拟币转换
        if(!isFastBuy(context)){
            return false;
        }

        return false;
    }

    private Boolean isFastBuy(Context context){
        PayinCreateOrderContext payinCreateOrderContext = (PayinCreateOrderContext) context;
        CreatePayinOrderRequest request = payinCreateOrderContext.getCreatePayinOrderRequest();
        return request.getOrderType().equalsIgnoreCase(TradeType.BUY.name());
    }

}
