package com.siupay.service.impl;

import com.siupay.constant.DynamicConstants;
import com.siupay.core.dto.BindInstrumentRequest;
import com.siupay.core.dto.BindInstrumentResponse;
import com.siupay.message.producer.client.SiuPayKafkaClient;
import com.siupay.service.DataCenterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DataCenterServiceImpl implements DataCenterService {

    @Autowired
    private DynamicConstants constants;

    @Autowired
    private SiuPayKafkaClient kafkaClient;

    @Override
    public void sendPreCardBindEvent(BindInstrumentRequest request) {
        try {
        } catch (Exception e) {
            log.error("DataCenterServiceImpl#sendPreCardBindEvent error:", e);
        }
    }

    @Override
    public void sendCardBindedEvent(BindInstrumentRequest request, BindInstrumentResponse response) {
        try {
        } catch (Exception e) {
            log.error("DataCenterServiceImpl#sendCardBindedEvent error:", e);
        }
    }

}
