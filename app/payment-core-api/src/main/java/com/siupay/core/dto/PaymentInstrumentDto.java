package com.siupay.core.dto;

import com.siupay.instrument.dto.card.Card;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
public class PaymentInstrumentDto {

    private String instrumentId;

    private String cardPanMaskToken;

    private String email;

    @NotNull
    @Valid
    private Card card;
}
