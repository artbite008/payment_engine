package com.siupay.merchant.admin.dto;

import lombok.Data;

/**
 * @Author: ce.liu
 * @Date: 2022/8/23 21:04
 */
@Data
public class AdminOrderFilterDto {

	private String mid;

	private String channel;

	private String currency;

	private String orderId;
}
