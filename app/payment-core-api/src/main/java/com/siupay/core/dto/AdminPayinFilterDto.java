package com.siupay.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminPayinFilterDto {

    private String orderType;

    private String fiatCurrency;

    private String cryptoCurrency;

    private String orderId;

    private String uid;

    private List<String> status;

    private String paymentMethod;

    private Date createTimeStart;

    private Date createTimeEnd;
}
