package com.siupay.merchant.dal.mybatis.entity;

import com.siupay.enums.FeeTypeEnum;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 
 * @Date: 2022/8/20 22:21
 */
@Data
public class MerchantFeeInfo {

	private FeeTypeEnum feeType;

	private BigDecimal fee;
}
