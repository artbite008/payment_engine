package com.siupay.core.dto;

import com.siupay.common.api.dto.BaseDto;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@EqualsAndHashCode
public class BindInstrumentRequest extends BaseDto {

    private String merchantId;
    private String email;

    private Boolean isDefault = true;

    @NotEmpty(message = "paymentMethod not empty!")
    private String paymentMethod;

    @NotNull
    @Valid
    private PaymentInstrumentDto paymentInstrument;

    @NotNull
    private AdditionalDataDto additionalData;
}
