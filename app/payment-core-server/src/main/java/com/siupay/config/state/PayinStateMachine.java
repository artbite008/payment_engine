package com.siupay.config.state;

import com.siupay.common.api.enums.PaymentSystem;
import com.siupay.common.api.exception.ErrorCode;
import com.siupay.common.api.exception.PaymentException;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.access.StateMachineAccess;
import org.springframework.statemachine.access.StateMachineFunction;
import org.springframework.statemachine.support.DefaultStateMachineContext;

/**
 * @Author:
 * @Date:
 * */
public class PayinStateMachine {

    private StateMachine machine;

    public final Enum getState(){
        return (Enum) this.machine.getState().getId();
    }

    public final void stop(){
        machine.stop();
    }

    public final PayinStateMachine sendEvent(Enum event) throws Exception {
        if (!this.machine.sendEvent(event)){
            throw new PaymentException(ErrorCode.STATUS_ERROR,"The payin order status cannot be "+event+" when the status is "+getState(), PaymentSystem.PAYMENT_CORE);
        }else{
            return this;
        }
    }

    public final void validate(Enum event){
        if (!this.machine.sendEvent(event)){
            throw new PaymentException(ErrorCode.STATUS_ERROR,"The payin order status cannot be "+event+" when the status is "+getState(), PaymentSystem.PAYMENT_CORE);
        }
    }

    public final PayinStateMachine restoreContext(final Enum state){
        this.machine.getStateMachineAccessor()
                .doWithRegion((new StateMachineFunction<StateMachineAccess>() {
                    @Override
                    public void apply(StateMachineAccess stateMachineAccess) {
                        ((StateMachineAccess)stateMachineAccess).resetStateMachine((
                            new DefaultStateMachineContext(state, null,null,null,null,PayinStateMachine.this.machine.getId())
                        ));
                    }
                }));
        return this;
    }

    public PayinStateMachine(StateMachine machine){
        this.machine = machine;
    }
}
