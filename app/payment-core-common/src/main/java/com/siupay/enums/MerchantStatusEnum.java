package com.siupay.enums;

/**
 * @Author: ce.liu
 * @Date: 2022/8/20 22:24
 */
public enum MerchantStatusEnum {

	ACTIVE("正常"),

	INACTIVE("禁用");

	private String desc;

	MerchantStatusEnum(String desc){
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}
}
