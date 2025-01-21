package com.siupay.merchant.dal.mybatis.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.siupay.common.lib.enums.ChannelEnum;
import com.siupay.common.lib.enums.PaymentMethod;
import com.siupay.enums.ApproveStatusEnum;
import com.siupay.enums.CertificationTypeEnum;
import com.siupay.enums.MerchantStatusEnum;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Author: ce.liu
 * @Date: 2022/8/20 22:15
 */
@Data
@TableName(value = "merchants",autoResultMap = true)
public class MerchantEntity {

	@TableId(type = IdType.INPUT)
	private String mid;

	private String password;

	private String firstName;

	private String lastName;

	private String email;

	private CertificationTypeEnum certificationType;

	private String certificationId;

	private String country;

	private MerchantStatusEnum status;

	@TableField(typeHandler = JacksonTypeHandler.class)
	private List<PaymentMethod> paymentMethod;

	@TableField(typeHandler = JacksonTypeHandler.class)
	private List<ChannelEnum> channel;

	@TableField(typeHandler = JacksonTypeHandler.class)
	private MerchantFeeInfo feeInfo;

	private ApproveStatusEnum approveStatus;

	private Date approvedTime;

	private Date createTime;

	private Date updateTime;
}
