package com.siupay.merchant.controller;

import com.siupay.common.api.dto.response.GenericResponse;
import com.siupay.common.lib.json.JsonUtils;
import com.siupay.merchant.front.MerchantQueryApi;
import com.siupay.merchant.front.dto.MerchantCountDto;
import com.siupay.merchant.front.dto.request.MerchantCountQueryRequest;
import com.siupay.merchant.service.MerchantQueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: ce.liu
 * @Date: 2022/8/21 21:42
 */
@Slf4j
@RestController
public class MerchantQueryController implements MerchantQueryApi {

	@Autowired
	private MerchantQueryService merchantQueryService;


	@Override
	public GenericResponse<MerchantCountDto> queryMerchantCountById(MerchantCountQueryRequest merchantCountQueryRequest) {
		log.info("query merchant count request = {}", JsonUtils.toJson(merchantCountQueryRequest));
		GenericResponse<MerchantCountDto> either = merchantQueryService.queryMerchantCountById(merchantCountQueryRequest);
		log.info("query merchant count response={}", JsonUtils.toJson(either));
		return either;
	}
}
