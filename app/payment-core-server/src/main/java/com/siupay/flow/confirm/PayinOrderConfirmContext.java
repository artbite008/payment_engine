package com.siupay.flow.confirm;

import com.siupay.core.dto.PayinOrderConfirmRequest;
import com.siupay.core.dto.PayinOrderConfirmResponse;
import com.siupay.flow.confirm.handler.ConfirmPayinOrderValidate;
import lombok.Data;
import org.apache.commons.chain.impl.ContextBase;

import java.util.Map;

@Data
public class PayinOrderConfirmContext extends ContextBase {

    private PayinOrderConfirmRequest request;

    private PayinOrderConfirmResponse response;

    private String channelCoreId;

    private String channelCoreStatus;

    private String redirectUrl;

    /**
     * 设值：
     * @see ConfirmPayinOrderValidate#execute(org.apache.commons.chain.Context)
     */
    private String paymentMethod;
    /**
     * 渠道返回的额外信息
     */
    private Map<String,Object> apmInfo;
}
