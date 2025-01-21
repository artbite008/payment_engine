package com.siupay.utils;

import com.siupay.common.api.exception.ErrorCode;
import com.siupay.common.api.exception.PaymentException;
import com.siupay.common.api.utils.MerchantContextUtils;
import com.siupay.common.api.utils.UserContextUtils;
import com.siupay.starter.chaincontext.ChainContextConstants;
import com.siupay.starter.chaincontext.ChainRequestContext;
import org.springframework.util.StringUtils;

public abstract class BaseFacade {

    protected String getUserId() {

        String userId = UserContextUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
//            HttpServletRequest request = ((ServletRequestAttributes)(RequestContextHolder.currentRequestAttributes())).getRequest();
//            userId = request.getHeader(ChainContextConstants.USER_ID);
//            if (StringUtils.isEmpty(userId)){
            throw new PaymentException(ErrorCode.USER_INFO_INVALID);
//            }
        }
        return userId;
    }

    protected String getMerchantId(){
        String merchantId = MerchantContextUtils.getMerchantId();
        if (StringUtils.isEmpty(merchantId)){
            throw new PaymentException(ErrorCode.USER_INFO_INVALID,"merchant info invalid");
        }
        return merchantId;
    }

    protected String getIp() {
        return ChainRequestContext.getCurrentContext().getString(ChainContextConstants.IP);
    }
    
    
}
