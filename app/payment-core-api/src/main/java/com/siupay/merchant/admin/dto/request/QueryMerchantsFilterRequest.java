package com.siupay.merchant.admin.dto.request;

import com.siupay.common.api.dto.request.BasePaginationRequest;
import com.siupay.merchant.admin.dto.MerchantFilterDto;
import lombok.Data;

/**
 * @Author: ce.liu
 * @Date: 2022/8/21 15:29
 */
@Data
public class QueryMerchantsFilterRequest extends BasePaginationRequest {

	private MerchantFilterDto filter;

}
