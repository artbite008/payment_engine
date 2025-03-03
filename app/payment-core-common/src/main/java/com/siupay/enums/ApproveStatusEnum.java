package com.siupay.enums;

/**
 * 
 * @Date: 2022/8/20 22:27
 */
public enum ApproveStatusEnum {

	VERIFIED("已认证"),

	UNVERIFIED("未认证");

	private String desc;

	ApproveStatusEnum(String desc){
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}
}
