package com.siupay.core.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.siupay.common.api.dto.PaymentAmount;
import lombok.Getter;
import lombok.Setter;

import java.text.DateFormat;
import java.util.Date;
import java.util.TimeZone;

@Getter
@Setter
public class PayinOrderDto {
    private String orderId;

    private String paymentInstrumentId;

    private String orderType;

    private String paymentMethod;

    private String channelId;

    private String channelCoreId;

    private String accountingBizId;

    private String status;

    private PaymentAmount payinAmount;

    private PaymentAmount feeAmount;

    private PaymentAmount depositFiatAmount;

    private PaymentAmount depositCryptoAmount;

    private Date createTime;

    private static final ObjectMapper MAPPER;

    static {
        final DateFormat iso8601 = new ISO8601DateFormat();
        iso8601.setTimeZone(TimeZone.getTimeZone("UTC"));
        MAPPER = new ObjectMapper().registerModule(new Jdk8Module()).setDateFormat(iso8601);
    }

    @Override
    public String toString() {
        try {
            return MAPPER.writeValueAsString(this);
        } catch (final JsonProcessingException ioe) {
            return ioe.getLocalizedMessage();
        }
    }
}
