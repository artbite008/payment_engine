package com.siupay.flow.async;

import com.siupay.channel.core.dto.record.PayinResultEventBo;
import lombok.Data;
import org.apache.commons.chain.impl.ContextBase;

@Data
public class PayinAsyncContext extends ContextBase {

    private PayinResultEventBo payinCreateResponse;

    private String payinOrderId;
}
