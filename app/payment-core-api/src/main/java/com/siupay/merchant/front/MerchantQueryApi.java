package com.siupay.merchant.front;

import com.siupay.common.api.dto.response.GenericResponse;
import com.siupay.core.common.dto.Paging;
import com.siupay.merchant.front.dto.MerchantCountDto;
import com.siupay.merchant.front.dto.request.MerchantCountQueryRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 
 * @Date: 2022/8/21 21:25
 */
@FeignClient(name ="PAYMENT-CORE", contextId = "MerchantQueryApi")
public interface MerchantQueryApi {

	@PostMapping("/v1/api/payment-core/front/query/merchantCount")
    GenericResponse<MerchantCountDto> queryMerchantCountById(@RequestBody MerchantCountQueryRequest merchantCountQueryRequest);
}
