package com.siupay.flow.order;

import com.siupay.common.lib.enums.ChannelEnum;
import com.siupay.core.dal.mybatis.entity.SpreadConfig;
import com.siupay.core.dto.CreatePayinOrderRequest;
import com.siupay.core.dto.CreatePayinOrderResponse;
import com.siupay.enums.RiskResultEnum;
import lombok.Data;
import org.apache.commons.chain.impl.ContextBase;

import java.math.BigDecimal;

@Data
public class PayinCreateOrderContext extends ContextBase {

    private CreatePayinOrderRequest createPayinOrderRequest;

    private CreatePayinOrderResponse createPayinOrderResponse;

    private RiskResultEnum riskResult;

    private ChannelEnum channel;

    private BigDecimal feeAmount;

    private String feeCurrency;

    private BigDecimal depositCryptoAmount;

    private String depositCryptoCurrency;

    private BigDecimal spreadAmount;

    private String spreadCurrency;

    private SpreadConfig spreadConfig;
}
