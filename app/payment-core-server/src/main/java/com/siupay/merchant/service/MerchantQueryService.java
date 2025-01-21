package com.siupay.merchant.service;

import com.siupay.common.api.dto.response.GenericResponse;
import com.siupay.merchant.front.dto.MerchantCountDto;
import com.siupay.merchant.front.dto.request.MerchantCountQueryRequest;

/**
 * @Author: ce.liu
 * @Date: 2022/8/21 21:55
 */
public interface MerchantQueryService {

	GenericResponse<MerchantCountDto> queryMerchantCountById(MerchantCountQueryRequest request);
}
