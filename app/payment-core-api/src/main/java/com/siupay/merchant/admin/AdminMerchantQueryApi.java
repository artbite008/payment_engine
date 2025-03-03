package com.siupay.merchant.admin;

import com.siupay.common.api.dto.response.BasePaginationResponse;
import com.siupay.common.api.dto.response.GenericResponse;
import com.siupay.merchant.admin.dto.AdminOrderDto;
import com.siupay.merchant.admin.dto.MerchantDto;
import com.siupay.merchant.admin.dto.request.QueryAdminOrdersFilterRequest;
import com.siupay.merchant.admin.dto.request.QueryMerchantsFilterRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 
 * @Date: 2022/8/20 23:02
 */
@FeignClient(name ="PAYMENT-CORE", contextId = "AdminMerchantQueryApi")
public interface AdminMerchantQueryApi {

	@PostMapping("/v1/api/payment-core/admin/query/merchant")
	GenericResponse<BasePaginationResponse<MerchantDto>> queryMerchants(@RequestBody QueryMerchantsFilterRequest queryMerchantsFilterRequest);

	@PostMapping("/v1/api/payment-core/admin/query/merchant/orders")
	GenericResponse<BasePaginationResponse<AdminOrderDto>> queryMerchantOrders(@RequestBody QueryAdminOrdersFilterRequest queryAdminOrdersFilterRequest);

}
