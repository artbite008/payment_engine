package com.siupay.flow.confirm.handler;

import com.siupay.common.api.exception.ErrorCode;
import com.siupay.common.api.exception.PaymentException;
import com.siupay.core.dal.mybatis.entity.PayinOrderEntity;
import com.siupay.core.dal.mybatis.repositry.PayinOrderRepository;
import com.siupay.core.dto.PayinOrderConfirmRequest;
import com.siupay.core.dto.PayinOrderConfirmResponse;
import com.siupay.enums.PayinOrderStatusEnum;
import com.siupay.flow.confirm.PayinOrderConfirmContext;
import com.siupay.utils.BaseFacade;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Slf4j
public class ConfirmPayinOrderValidate extends BaseFacade implements Command {

    @Autowired
    private PayinOrderRepository payinOrderRepository;

    @Override
    public boolean execute(Context context) throws Exception {
        PayinOrderConfirmContext confirmContext = (PayinOrderConfirmContext) context;
        PayinOrderConfirmRequest request = confirmContext.getRequest();
        PayinOrderEntity payinOrder = payinOrderRepository.getById(request.getOrderId());
        if (Objects.isNull(payinOrder)){
            throw new PaymentException(ErrorCode.RECORD_NOT_EXIST);
        }
        confirmContext.setPaymentMethod(payinOrder.getPaymentMethod());
        if (PayinOrderStatusEnum.CREATED.name().equals(payinOrder.getStatus())){
            return false;
            //throw new PaymentException(ErrorCode.RECORD_ALREADY_EXISTS);
        }else{
            PayinOrderConfirmResponse response = new PayinOrderConfirmResponse();
            response.setOrderId(payinOrder.getPayinOrderId());
            response.setStatus(payinOrder.getStatus());
            confirmContext.setResponse(response);
            return true;
        }
    }

    private void checkPayinOrder(Context context){

    }
}
