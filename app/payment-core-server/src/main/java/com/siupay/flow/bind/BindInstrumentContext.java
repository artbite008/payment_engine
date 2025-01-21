package com.siupay.flow.bind;

import com.siupay.channel.core.model.CardInfo;
import com.siupay.common.lib.enums.ChannelEnum;
import com.siupay.core.dto.BindInstrumentRequest;
import com.siupay.core.dto.BindInstrumentResponse;
import com.siupay.enums.RiskResultEnum;
import lombok.Data;
import org.apache.commons.chain.impl.ContextBase;

@Data
public class BindInstrumentContext extends ContextBase {

    private BindInstrumentRequest bindInstrumentRequest;

    private BindInstrumentResponse bindInstrumentResponse;

    private String channelTokenId;

    private ChannelEnum channel;

    private CardInfo cardInfo;

    private RiskResultEnum riskResult;
    /**
     * succeeded,failed
     */
    private String bindStatus;
}
