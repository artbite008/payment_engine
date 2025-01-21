package com.siupay.config.state;

import com.siupay.common.api.enums.PaymentSystem;
import com.siupay.common.api.exception.ErrorCode;
import com.siupay.common.api.exception.PaymentException;
import com.siupay.core.dal.mybatis.entity.PayinOrderEntity;
import com.siupay.enums.PayinOrderPayEventsEnum;
import com.siupay.enums.PayinOrderStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;

@Service
@Slf4j
public class PayinStateMachineService {

    @Resource
    private StateMachineFactory<PayinOrderStatusEnum, PayinOrderPayEventsEnum> stateMachineFactory;

    public PayinOrderStatusEnum getInitState(){
        return stateMachineFactory.getStateMachine().getInitialState().getId();
    }

    public PayinStateMachineService sentEvent(PayinOrderEntity payinOrder, PayinOrderPayEventsEnum event){
        try {
            log.info("Receive state change even{}, payinOrderId={}, status is {}",event,payinOrder.getPayinOrderId(),payinOrder.getStatus());
            PayinStateMachine stateMachine = getStateMachine(payinOrder);
            payinOrder.setStatus(((PayinOrderStatusEnum) stateMachine.sendEvent(event).getState()).name());
            stateMachine.stop();
            log.info("payin order event done status is {}",payinOrder.getStatus());
        }catch (Exception e){
            throw new PaymentException(ErrorCode.STATUS_ERROR, Optional.ofNullable(e.getMessage()).orElse(ErrorCode.STATUS_ERROR.getMsg()), PaymentSystem.PAYMENT_CORE);
        }
        return this;
    }

    private PayinStateMachine getStateMachine(PayinOrderEntity payinOrder){
        try {
            StateMachine machine = stateMachineFactory.getStateMachine(payinOrder.getPayinOrderId());
            PayinStateMachine stateMachine = new PayinStateMachine(machine);
            stateMachine.restoreContext(PayinOrderStatusEnum.valueOf(payinOrder.getStatus()));
            return stateMachine;
        }catch (Exception e){
            throw new PaymentException(ErrorCode.STATUS_ERROR,e.getMessage(),PaymentSystem.PAYMENT_CORE);
        }
    }
}
