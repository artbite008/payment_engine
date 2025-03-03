package com.siupay.merchant.admin.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;

/**
 * 
 * @Date: 2022/8/20 23:03
 */
@Data
@ApiModel("merchant info")
public class MerchantDto {

	private String mid;

	private String email;

	private String name;

	private String country;

	private String certificationId;

	private ApproveStatus approveStatus;

	private MerchantStatus merchantStatus;

	private Date approvedTime;
}
