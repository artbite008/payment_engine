package com.siupay.merchant.front.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 
 * @Date: 2022/8/26 20:35
 */
@Data
public class MerchantCountQueryRequest {

	@NotBlank(message = "mid not empty")
	private String mid;

	@NotBlank(message = "start date not empty")
	private String startDate;

//	@NotBlank(message = "end date not empty")
	private String endDate;

	@NotBlank
	private String currency;
}
