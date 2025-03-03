package com.siupay.merchant.admin.dto;

import com.siupay.common.api.dto.PaymentAmount;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;

/**
 * 
 * @Date: 2022/8/23 20:52
 */
@Data
@ApiModel("admin order info")
public class AdminOrderDto {

	private String mid;

	private String merchantName;

	private String orderId;

	private Date paymentTime;

	private String channel;

	private String paymentMethod;

	private PaymentAmount amount;

	private PaymentAmount fee;

	private String orderStatus;

	private String orderType;

}
