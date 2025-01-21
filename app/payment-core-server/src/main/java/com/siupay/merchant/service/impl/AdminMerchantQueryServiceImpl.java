package com.siupay.merchant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.siupay.accounting.api.request.TransactionType;
import com.siupay.common.api.dto.PaymentAmount;
import com.siupay.common.api.dto.response.BasePaginationResponse;
import com.siupay.common.api.enums.PaymentSystem;
import com.siupay.common.api.exception.ErrorCode;
import com.siupay.common.api.exception.PaymentError;
import com.siupay.common.lib.utils.BigDecimalUtils;
import com.siupay.common.api.dto.response.GenericResponse;
import com.siupay.core.dal.mybatis.entity.PayinOrderEntity;
import com.siupay.core.dal.mybatis.repositry.PayinOrderRepository;
import com.siupay.merchant.admin.dto.AdminOrderDto;
import com.siupay.merchant.admin.dto.ApproveStatus;
import com.siupay.merchant.admin.dto.MerchantDto;
import com.siupay.merchant.admin.dto.MerchantStatus;
import com.siupay.merchant.admin.dto.request.QueryAdminOrdersFilterRequest;
import com.siupay.merchant.admin.dto.request.QueryMerchantsFilterRequest;
import com.siupay.merchant.dal.mybatis.entity.MerchantEntity;
import com.siupay.merchant.dal.mybatis.repositry.MerchantRepository;
import com.siupay.merchant.service.AdminMerchantQueryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Author: ce.liu
 * @Date: 2022/8/20 23:11
 */
@Service
public class AdminMerchantQueryServiceImpl implements AdminMerchantQueryService {

	@Autowired
	private MerchantRepository merchantRepository;
	@Autowired
	private PayinOrderRepository payinOrderRepository;


	@Override
	public GenericResponse<MerchantDto> queryMerchantById(String mid) {

		MerchantEntity merchant = merchantRepository.getById(mid);
		if (Objects.isNull(merchant)){
			PaymentError paymentError = new PaymentError(
					ErrorCode.RECORD_NOT_EXIST.getCode(),
					ErrorCode.RECORD_NOT_EXIST.getMsg(),
					PaymentSystem.PAYMENT_CORE.getSystemId()
			);
			return GenericResponse.fail(paymentError.getCode(),paymentError.getMsg());
		}
		return GenericResponse.success(merchantEntity2MerchantDto(merchant));
	}

	@Override
	public GenericResponse<BasePaginationResponse<MerchantDto>> queryMerchants(QueryMerchantsFilterRequest request) {
		Page<MerchantEntity> page = new Page<>(request.getCurrentPage(),request.getPageSize());
		LambdaQueryWrapper<MerchantEntity> wrapper = getMerchantEntityWrapper(request);
		merchantRepository.page(page,wrapper);
		BasePaginationResponse<MerchantDto> paging = new BasePaginationResponse<>();
		paging.setCurrentPage(request.getCurrentPage());
		paging.setPageSize(request.getPageSize());
		paging.setTotalPage(new Long(page.getPages()).intValue());
		paging.setTotalNum(new Long(page.getTotal()).intValue());
		List<MerchantDto> list = page.getRecords().stream().map(this::merchantEntity2MerchantDto).collect(Collectors.toList());
		paging.setItems(list);
		return GenericResponse.success(paging);
	}

	@Override
	public GenericResponse<BasePaginationResponse<AdminOrderDto>> queryOrders(QueryAdminOrdersFilterRequest request) {
		Page<PayinOrderEntity> page = new Page<>(request.getCurrentPage(),request.getPageSize());
		LambdaQueryWrapper<PayinOrderEntity> wrapper = getOrderEntityWrapper(request);
		payinOrderRepository.page(page,wrapper);
		BasePaginationResponse<AdminOrderDto> paging = new BasePaginationResponse<>();
		paging.setCurrentPage(request.getCurrentPage());
		paging.setPageSize(request.getPageSize());
		paging.setTotalPage(new Long(page.getPages()).intValue());
		paging.setTotalNum(new Long(page.getTotal()).intValue());
		List<AdminOrderDto> list = page.getRecords().stream().map(this::payinOrderEntity2AdminOrderDto).collect(Collectors.toList());
		paging.setItems(list);
		return GenericResponse.success(paging);
	}

	private LambdaQueryWrapper<MerchantEntity> getMerchantEntityWrapper(QueryMerchantsFilterRequest request){
		LambdaQueryWrapper<MerchantEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();

		lambdaQueryWrapper.orderByDesc(MerchantEntity::getCreateTime);
		if (Objects.nonNull(request.getFilter())){
			if (Objects.nonNull(request.getFilter().getMid())){
				lambdaQueryWrapper.eq(MerchantEntity::getMid,request.getFilter().getMid());
			}
			if (Objects.nonNull(request.getFilter().getEmail())){
				lambdaQueryWrapper.eq(MerchantEntity::getEmail,request.getFilter().getEmail());
			}
		}
		return lambdaQueryWrapper;
	}

	private LambdaQueryWrapper<PayinOrderEntity> getOrderEntityWrapper(QueryAdminOrdersFilterRequest request){
		LambdaQueryWrapper<PayinOrderEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();

		lambdaQueryWrapper.orderByDesc(PayinOrderEntity::getCreateTime);
		if (Objects.nonNull(request.getFilter())){
			if (Objects.nonNull(request.getFilter().getMid())){
				lambdaQueryWrapper.eq(PayinOrderEntity::getMerchantId,request.getFilter().getMid());
			}
			if (Objects.nonNull(request.getFilter().getOrderId())){
				lambdaQueryWrapper.eq(PayinOrderEntity::getPayinOrderId,request.getFilter().getOrderId());
			}
			if (Objects.nonNull(request.getFilter().getChannel())){
				lambdaQueryWrapper.eq(PayinOrderEntity::getChannelId,request.getFilter().getChannel());
			}
			if (Objects.nonNull(request.getFilter().getCurrency())){
				lambdaQueryWrapper.eq(PayinOrderEntity::getPayinCurrency,request.getFilter().getCurrency());
			}
		}
		return lambdaQueryWrapper;
	}


	private MerchantDto merchantEntity2MerchantDto(MerchantEntity merchant){
		MerchantDto merchantDto = new MerchantDto();
		BeanUtils.copyProperties(merchant,merchantDto);
		merchantDto.setName(merchant.getFirstName());
		MerchantStatus merchantStatus = new MerchantStatus();
		merchantStatus.setKey(merchant.getStatus().name());
		merchantStatus.setValue(merchant.getStatus().getDesc());
		merchantDto.setMerchantStatus(merchantStatus);
		ApproveStatus approveStatus = new ApproveStatus();
		approveStatus.setKey(merchant.getApproveStatus().name());
		approveStatus.setValue(merchant.getApproveStatus().getDesc());
		merchantDto.setApproveStatus(approveStatus);
		return merchantDto;
	}

	private AdminOrderDto payinOrderEntity2AdminOrderDto(PayinOrderEntity payinOrder){
		AdminOrderDto adminOrderDto = new AdminOrderDto();
		adminOrderDto.setOrderId(payinOrder.getPayinOrderId());
		adminOrderDto.setChannel(payinOrder.getChannelId());
		adminOrderDto.setMid(payinOrder.getMerchantId());
		MerchantEntity merchant = merchantRepository.getById(payinOrder.getMerchantId());
		if (Objects.nonNull(merchant)){
			adminOrderDto.setMerchantName(merchant.getFirstName());
		}
		adminOrderDto.setPaymentTime(payinOrder.getCreateTime());
		adminOrderDto.setPaymentMethod(payinOrder.getPaymentMethod());
		if (StringUtils.isNotEmpty(payinOrder.getPayinCurrency())){
			PaymentAmount paymentAmount = new PaymentAmount();
			paymentAmount.setAmount(BigDecimalUtils.currencyParse(payinOrder.getPayinAmount(),payinOrder.getPayinCurrency()));
			paymentAmount.setCurrency(payinOrder.getPayinCurrency());
			adminOrderDto.setAmount(paymentAmount);
		}
		if (StringUtils.isNotEmpty(payinOrder.getFeeCurrency())){
			PaymentAmount feeAmount = new PaymentAmount();
			feeAmount.setAmount(BigDecimalUtils.currencyParse(payinOrder.getFeeAmount(),payinOrder.getFeeCurrency()));
			feeAmount.setCurrency(payinOrder.getFeeCurrency());
			adminOrderDto.setFee(feeAmount);
		}
		adminOrderDto.setOrderStatus(payinOrder.getStatus());
		adminOrderDto.setOrderType("Payment");
		return adminOrderDto;
	}
}
