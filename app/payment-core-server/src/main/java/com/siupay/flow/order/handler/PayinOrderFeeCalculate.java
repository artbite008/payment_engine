package com.siupay.flow.order.handler;

import com.siupay.common.api.exception.ErrorCode;
import com.siupay.common.api.exception.PaymentException;
import com.siupay.core.dto.CreatePayinOrderRequest;
import com.siupay.flow.order.PayinCreateOrderContext;
import com.siupay.merchant.dal.mybatis.entity.MerchantEntity;
import com.siupay.merchant.dal.mybatis.repositry.MerchantRepository;
import com.siupay.utils.BaseFacade;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Objects;

@Component
@Slf4j
public class PayinOrderFeeCalculate extends BaseFacade implements Command {

    @Autowired
    private MerchantRepository merchantRepository;

    @Override
    public boolean execute(Context context) throws Exception {
        feeCalculate(context);
        return false;
    }

    private void feeCalculate(Context context){
        CreatePayinOrderRequest request = ((PayinCreateOrderContext) context).getCreatePayinOrderRequest();

        MerchantEntity merchant = merchantRepository.getById(getMerchantId());
        if (Objects.nonNull(merchant)&&Objects.nonNull(merchant.getFeeInfo())){
            BigDecimal feeAmount = request.getPaymentAmount().getAmount().multiply(merchant.getFeeInfo().getFee());
            //未完成需确认接口
            ((PayinCreateOrderContext) context).setFeeAmount(feeAmount);
            ((PayinCreateOrderContext) context).setFeeCurrency(request.getPaymentAmount().getCurrency());
        }else {
            //未完成需确认接口
            throw new PaymentException(ErrorCode.VALIDATE_ERROR,"mid not exist");
        }
    }
}
