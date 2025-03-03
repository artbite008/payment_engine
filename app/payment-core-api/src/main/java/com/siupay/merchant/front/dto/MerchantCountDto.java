package com.siupay.merchant.front.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 
 * @Date: 2022/8/21 21:48
 */
@Data
public class MerchantCountDto {

	private String currency;

	private BigDecimal payinOrderTotalAmount;

	private Long payinOrderTotalCount;

	private BigDecimal refundTotalAmount;

	private Long refundTotalCount;

	private BigDecimal chargebackTotalAmount;

	private Long chargebackTotalCount;


}
