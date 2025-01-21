package com.siupay.merchant.controller;

import com.siupay.common.api.dto.response.BasePaginationResponse;
import com.siupay.common.lib.json.JsonUtils;
import com.siupay.common.api.dto.response.GenericResponse;
import com.siupay.merchant.admin.AdminMerchantQueryApi;
import com.siupay.merchant.admin.dto.AdminOrderDto;
import com.siupay.merchant.admin.dto.MerchantDto;
import com.siupay.merchant.admin.dto.request.QueryAdminOrdersFilterRequest;
import com.siupay.merchant.admin.dto.request.QueryMerchantsFilterRequest;
import com.siupay.merchant.service.AdminMerchantQueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: ce.liu
 * @Date: 2022/8/20 23:08
 */
@Slf4j
@RestController
public class AdminMerchantQueryController implements AdminMerchantQueryApi {

	@Autowired
	private AdminMerchantQueryService adminMerchantQueryService;


	@Override
	public GenericResponse<BasePaginationResponse<MerchantDto>> queryMerchants(QueryMerchantsFilterRequest queryMerchantsFilterRequest) {
		log.info("query merchant list request = {}", JsonUtils.toJson(queryMerchantsFilterRequest));
		GenericResponse<BasePaginationResponse<MerchantDto>> either = adminMerchantQueryService.queryMerchants(queryMerchantsFilterRequest);
		log.info("query merchant list response={}", JsonUtils.toJson(either));
		return either;
	}

	@Override
	public GenericResponse<BasePaginationResponse<AdminOrderDto>> queryMerchantOrders(QueryAdminOrdersFilterRequest queryAdminOrdersFilterRequest) {
		log.info("query admin order list request = {}", JsonUtils.toJson(queryAdminOrdersFilterRequest));
		GenericResponse<BasePaginationResponse<AdminOrderDto>> either = adminMerchantQueryService.queryOrders(queryAdminOrdersFilterRequest);
		log.info("query merchant list response={}", JsonUtils.toJson(either));
		return either;
	}
}
