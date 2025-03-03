package com.siupay.facade.client;

import com.siupay.common.api.dto.response.GenericResponse;
import com.siupay.facade.client.dto.UserKycInfoResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 
 * @date 2022年01月26日
 */
@FeignClient("${application.ucenter:UCENTER}")
public interface UserKycClient {

    @GetMapping("/inner/kyc-info")
    GenericResponse<UserKycInfoResponseDTO> queryKycInfo();
}
