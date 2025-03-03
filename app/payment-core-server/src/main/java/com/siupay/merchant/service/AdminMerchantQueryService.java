package com.siupay.merchant.service;

import com.siupay.common.api.dto.response.BasePaginationResponse;
import com.siupay.common.api.dto.response.GenericResponse;
import com.siupay.merchant.admin.dto.AdminOrderDto;
import com.siupay.merchant.admin.dto.MerchantDto;
import com.siupay.merchant.admin.dto.request.QueryAdminOrdersFilterRequest;
import com.siupay.merchant.admin.dto.request.QueryMerchantsFilterRequest;

/**
 * 
 * @Date: 2022/8/20 23:11
 */
public interface AdminMerchantQueryService {

	GenericResponse<MerchantDto> queryMerchantById(String mid);

	GenericResponse<BasePaginationResponse<MerchantDto>> queryMerchants(QueryMerchantsFilterRequest request);

	GenericResponse<BasePaginationResponse<AdminOrderDto>> queryOrders(QueryAdminOrdersFilterRequest request);
}
