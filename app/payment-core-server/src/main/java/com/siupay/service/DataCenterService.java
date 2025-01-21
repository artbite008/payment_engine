package com.siupay.service;

import com.siupay.core.dto.BindInstrumentRequest;
import com.siupay.core.dto.BindInstrumentResponse;

public interface DataCenterService {
    void sendPreCardBindEvent(BindInstrumentRequest bindInstrumentRequest);
    void sendCardBindedEvent(BindInstrumentRequest bindInstrumentRequest,BindInstrumentResponse response);
}
