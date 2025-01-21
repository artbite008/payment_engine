package com.siupay.core.dto;

import com.siupay.common.api.dto.BaseDto;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class BindInstrumentResponse extends BaseDto {

    private String channelId;

    private PaymentInstrumentDto paymentInstrument;

}
