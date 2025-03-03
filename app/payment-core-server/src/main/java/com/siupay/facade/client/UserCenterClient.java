package com.siupay.facade.client;

import com.siupay.common.lib.json.JsonUtils;
import com.siupay.facade.client.dto.UserKycInfoResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @program: deposit
 * @description: 获取用户基本信息
 * 
 * @create: 2021-05-11 15:39
 **/
@Component
@Slf4j
public class UserCenterClient {
    @Autowired
    private UserKycClient userKycClient;

    public UserKycInfoResponseDTO queryKycInfo() {
        log.error("UserCenterClient获取用户kyc信息");
        try {

        } catch (Exception ex) {
            log.error("UserCenterClient获取用户kyc信息失败", ex);
        }
        return null;
    }
}
