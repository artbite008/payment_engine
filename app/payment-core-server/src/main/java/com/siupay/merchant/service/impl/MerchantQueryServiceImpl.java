package com.siupay.merchant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.siupay.common.api.dto.response.GenericResponse;
import com.siupay.common.lib.utils.BigDecimalUtils;
import com.siupay.core.dal.mybatis.entity.PayinOrderEntity;
import com.siupay.core.dal.mybatis.repositry.PayinOrderRepository;
import com.siupay.merchant.front.dto.MerchantCountDto;
import com.siupay.merchant.front.dto.request.MerchantCountQueryRequest;
import com.siupay.merchant.service.MerchantQueryService;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

import static com.siupay.enums.PayinOrderStatusEnum.SUCCEEDED;

/**
 * @Author: ce.liu
 * @Date: 2022/8/21 21:56
 */
@Service
@Slf4j
public class MerchantQueryServiceImpl implements MerchantQueryService {

	@Autowired
	private PayinOrderRepository payinOrderRepository;


	@Override
	public GenericResponse<MerchantCountDto> queryMerchantCountById(MerchantCountQueryRequest request) {
		MerchantCountDto merchantCountDto =	queryMerchantCountDto(request);
		return GenericResponse.success(merchantCountDto);
	}

	private MerchantCountDto queryMerchantCountDto(MerchantCountQueryRequest request){

		String startTime = null;
		String endTime = null;
		if (Objects.nonNull(request.getStartDate())) {
			startTime = request.getStartDate() + " 00:00:00";
		}
		if (Objects.nonNull(request.getEndDate())) {
			endTime = request.getEndDate() + " 23:59:59";
		}else {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String strDate = dateFormat.format(new Date());
			endTime = strDate + " 23:59:59";
		}

		MerchantCountDto merchantCountDto = new MerchantCountDto();
		if (StringUtils.isEmpty(request.getCurrency())){
			merchantCountDto.setCurrency("USD");
		}else {
			merchantCountDto.setCurrency(request.getCurrency());
		}

		LambdaQueryWrapper<PayinOrderEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
		lambdaQueryWrapper.eq(PayinOrderEntity::getMerchantId,request.getMid());
		lambdaQueryWrapper.ge(PayinOrderEntity::getCreateTime,startTime);
		lambdaQueryWrapper.le(PayinOrderEntity::getCreateTime,endTime);
		lambdaQueryWrapper.eq(PayinOrderEntity::getStatus,SUCCEEDED.name());
		lambdaQueryWrapper.eq(PayinOrderEntity::getPayinCurrency,merchantCountDto.getCurrency());

		merchantCountDto.setPayinOrderTotalCount(payinOrderRepository.count(lambdaQueryWrapper));

		Long payinAmount = payinOrderRepository.getMerchantCount(request.getMid(),startTime,endTime);
		if (Objects.nonNull(payinAmount)){
			merchantCountDto.setPayinOrderTotalAmount(
					BigDecimalUtils.currencyParse(Long.parseLong(payinAmount.toString()),merchantCountDto.getCurrency())
			);
		}else {
			merchantCountDto.setPayinOrderTotalAmount(new BigDecimal(0));
		}
		merchantCountDto.setRefundTotalCount(0L);
		merchantCountDto.setRefundTotalAmount(new BigDecimal(0));
		merchantCountDto.setChargebackTotalCount(0L);
		merchantCountDto.setChargebackTotalAmount(new BigDecimal(0));

		return merchantCountDto;
	}
}
