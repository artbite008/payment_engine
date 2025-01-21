package com.siupay.flow.bind.handler;

import com.siupay.common.api.exception.ErrorCode;
import com.siupay.common.api.exception.PaymentException;
import com.siupay.core.dto.BindInstrumentRequest;
import com.siupay.facade.client.UserCenterClient;
import com.siupay.facade.client.dto.UserKycInfoResponseDTO;
import com.siupay.flow.bind.BindInstrumentContext;
import com.siupay.utils.BaseFacade;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
@Slf4j
public class BindInstrumentKYCValidate extends BaseFacade implements Command {

    @Autowired
    private UserCenterClient userCenterClient;

    @Override
    public boolean execute(Context context) throws Exception {
        BindInstrumentRequest bindInstrumentRequest = ((BindInstrumentContext) context).getBindInstrumentRequest();
        checkKyc(bindInstrumentRequest);
        return false;
    }

    private void checkKyc(BindInstrumentRequest request) {
        UserKycInfoResponseDTO userKycInfoResponseDTO = userCenterClient.queryKycInfo();
        if (Objects.isNull(userKycInfoResponseDTO) || Optional.ofNullable(request.getPaymentInstrument().getCard()).orElse(null) == null) {
            throw new PaymentException(ErrorCode.BUSINESS_ERROR, "绑卡kyc姓名校验未通过");
        }

        // 姓名判断
        String firstName = StringUtils.deleteWhitespace(Optional.ofNullable(request.getPaymentInstrument().getCard().getFirstName()).orElse("")).toUpperCase();
        String lastName = StringUtils.deleteWhitespace(Optional.ofNullable(request.getPaymentInstrument().getCard().getLastName()).orElse("")).toUpperCase();
        String kycFirstName = Optional.ofNullable(userKycInfoResponseDTO.getFirstName())
                .map(item -> StringUtils.deleteWhitespace(item).toUpperCase())
                .orElse(null);
        String kycLastName = Optional.ofNullable(userKycInfoResponseDTO.getLastName())
                .map(item -> StringUtils.deleteWhitespace(item).toUpperCase())
                .orElse(null);
        // kyc姓名都为null的情况放过
        if (StringUtils.isBlank(kycFirstName) && StringUtils.isBlank(kycLastName)) {
            return;
        }
        // 有一项匹配则放过
        if (firstName.equals(kycFirstName) || lastName.equals(kycLastName)) {
            return;
        }
        // 反向匹配都满足也放过
        if (firstName.equals(kycLastName) && lastName.equals(kycFirstName)) {
            return;
        }
        log.info("[绑卡kyc姓名校验]-校验失败，[user]-firstName={},lastName={},[KYC]-firstName={},lastName={}", firstName, lastName,
                kycFirstName, kycLastName);
        throw new PaymentException(ErrorCode.USER_KYC_LEVEL, "绑卡kyc姓名校验未通过");
    }
}
