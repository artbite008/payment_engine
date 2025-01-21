package com.siupay.config.state;

import com.siupay.enums.PayinOrderPayEventsEnum;
import com.siupay.enums.PayinOrderStatusEnum;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import java.util.EnumSet;

@Configuration
@EnableStateMachineFactory
public class PayinStateMachineConfig extends EnumStateMachineConfigurerAdapter<PayinOrderStatusEnum, PayinOrderPayEventsEnum> {

    @Override
    public void configure(StateMachineConfigurationConfigurer<PayinOrderStatusEnum, PayinOrderPayEventsEnum> config) throws Exception {
        config.withConfiguration().autoStartup(true);
    }

    @Override
    public void configure(StateMachineStateConfigurer<PayinOrderStatusEnum, PayinOrderPayEventsEnum> states) throws Exception {
        states.withStates()
                .initial(PayinOrderStatusEnum.CREATED)
                .end(PayinOrderStatusEnum.SUCCEEDED)
                .end(PayinOrderStatusEnum.FAILED)
                .end(PayinOrderStatusEnum.EXPIRED)
                .end(PayinOrderStatusEnum.CANCELLED)
                .states(EnumSet.allOf(PayinOrderStatusEnum.class));
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<PayinOrderStatusEnum, PayinOrderPayEventsEnum> transitions) throws Exception {

        //支付流转
        //异步通知先到达
        transitions.withInternal().source(PayinOrderStatusEnum.PAYIN_COMPLETED).event(PayinOrderPayEventsEnum.PROCESSING)
                .and().withInternal().source(PayinOrderStatusEnum.PAYIN_COMPLETED).event(PayinOrderPayEventsEnum.PENDING)
                .and().withInternal().source(PayinOrderStatusEnum.PAYIN_COMPLETED).event(PayinOrderPayEventsEnum.SUCCEEDED)//TODO withExternal要改成withInternal
                .and().withInternal().source(PayinOrderStatusEnum.FAILED).event(PayinOrderPayEventsEnum.FAILED)
                .and().withInternal().source(PayinOrderStatusEnum.SUCCEEDED).event(PayinOrderPayEventsEnum.SUCCEEDED)

                .and().withExternal()
                .source(PayinOrderStatusEnum.CREATED).target(PayinOrderStatusEnum.PROCESSING).event(PayinOrderPayEventsEnum.PROCESSING)
                .and().withExternal()
                .source(PayinOrderStatusEnum.CREATED).target(PayinOrderStatusEnum.PAYIN_COMPLETED).event(PayinOrderPayEventsEnum.SUCCEEDED)
                .and().withExternal()
                .source(PayinOrderStatusEnum.CREATED).target(PayinOrderStatusEnum.CUSTOMER_ACTION).event(PayinOrderPayEventsEnum.PENDING)
                .and().withExternal()
                .source(PayinOrderStatusEnum.CREATED).target(PayinOrderStatusEnum.FAILED).event(PayinOrderPayEventsEnum.FAILED)
                .and().withExternal()
                .source(PayinOrderStatusEnum.CREATED).target(PayinOrderStatusEnum.EXPIRED).event(PayinOrderPayEventsEnum.EXPIRED)
                .and().withExternal()
                .source(PayinOrderStatusEnum.PROCESSING).target(PayinOrderStatusEnum.PAYIN_COMPLETED).event(PayinOrderPayEventsEnum.SUCCEEDED)
                .and().withExternal()
                .source(PayinOrderStatusEnum.PROCESSING).target(PayinOrderStatusEnum.FAILED).event(PayinOrderPayEventsEnum.FAILED)
                .and().withExternal()
                .source(PayinOrderStatusEnum.CUSTOMER_ACTION).target(PayinOrderStatusEnum.PAYIN_COMPLETED).event(PayinOrderPayEventsEnum.SUCCEEDED)
                .and().withExternal()
                .source(PayinOrderStatusEnum.CUSTOMER_ACTION).target(PayinOrderStatusEnum.FAILED).event(PayinOrderPayEventsEnum.FAILED)
                .and().withExternal()
                .source(PayinOrderStatusEnum.CUSTOMER_ACTION).target(PayinOrderStatusEnum.EXPIRED).event(PayinOrderPayEventsEnum.EXPIRED)

                //上分流转,调用账务，账务保证成功失败
                .and().withExternal()
                .source(PayinOrderStatusEnum.PAYIN_COMPLETED).target(PayinOrderStatusEnum.SUCCEEDED).event(PayinOrderPayEventsEnum.DEPOSIT_SUCCESS)
                .and().withExternal()
                .source(PayinOrderStatusEnum.PAYIN_COMPLETED).target(PayinOrderStatusEnum.FAILED).event(PayinOrderPayEventsEnum.DEPOSIT_FAIL)

                //快捷买币,调用账务，账务保证成功失败
                .and().withExternal()
                .source(PayinOrderStatusEnum.PAYIN_COMPLETED).target(PayinOrderStatusEnum.SUCCEEDED).event(PayinOrderPayEventsEnum.CONVERT_SUCCESS)
                .and().withExternal()
                .source(PayinOrderStatusEnum.PAYIN_COMPLETED).target(PayinOrderStatusEnum.FAILED).event(PayinOrderPayEventsEnum.CONVERT_FAIL)
                
                //PCC撮合流程
                //PCC异步撮合中
                .and().withExternal()
                .source(PayinOrderStatusEnum.PAYIN_COMPLETED).target(PayinOrderStatusEnum.PCC_ORDER_CRYPTO_PROCESSING).event(PayinOrderPayEventsEnum.PCC_ORDER_CRYPTO_PROCESSING)
                //PCC撮合成功
                .and().withExternal()//正常从PROCESSING到SUCCESS
                .source(PayinOrderStatusEnum.PCC_ORDER_CRYPTO_PROCESSING).target(PayinOrderStatusEnum.SUCCEEDED).event(PayinOrderPayEventsEnum.PCC_ORDER_CRYPTO_SUCCESS)
                .and().withExternal()//先收到PCC kafka成功消息，直接从PAYIN_COMPLETED到SUCCEEDED
                .source(PayinOrderStatusEnum.PAYIN_COMPLETED).target(PayinOrderStatusEnum.SUCCEEDED).event(PayinOrderPayEventsEnum.PCC_ORDER_CRYPTO_SUCCESS)
                
                .and().withInternal()//先收到PCC kafka成功消息，再收到PROCESSING event不做状态流转处理
                .source(PayinOrderStatusEnum.SUCCEEDED).event(PayinOrderPayEventsEnum.PCC_ORDER_CRYPTO_PROCESSING)
                .and().withInternal()//重复收到PCC kafka成功消息，不做状态流转处理
                .source(PayinOrderStatusEnum.SUCCEEDED).event(PayinOrderPayEventsEnum.PCC_ORDER_CRYPTO_SUCCESS)
                //PCC撮合失败，开始反撮
                .and().withExternal()//正常从PROCESSING到FAIL
                .source(PayinOrderStatusEnum.PCC_ORDER_CRYPTO_PROCESSING).target(PayinOrderStatusEnum.PCC_ORDER_CRYPTO_FAIL).event(PayinOrderPayEventsEnum.PCC_ORDER_CRYPTO_FAIL)
                .and().withExternal()//先收到PCC kafka失败消息，直接从PAYIN_COMPLETED到FAIL
                .source(PayinOrderStatusEnum.PAYIN_COMPLETED).target(PayinOrderStatusEnum.PCC_ORDER_CRYPTO_FAIL).event(PayinOrderPayEventsEnum.PCC_ORDER_CRYPTO_FAIL)

                .and().withInternal()//先收到PCC kafka失败消息，再收到PROCESSING event不做状态流转处理
                .source(PayinOrderStatusEnum.PCC_ORDER_CRYPTO_FAIL).event(PayinOrderPayEventsEnum.PCC_ORDER_CRYPTO_PROCESSING)
                .and().withInternal()//重复收到PCC kafka失败消息，不做状态流转处理
                .source(PayinOrderStatusEnum.PCC_ORDER_CRYPTO_FAIL).event(PayinOrderPayEventsEnum.PCC_ORDER_CRYPTO_FAIL)
                //PCC反撮完成，取消预授权
                .and().withExternal()//job轮训channel-core查询订单状态，修改结果
                .source(PayinOrderStatusEnum.PCC_ORDER_CRYPTO_FAIL).target(PayinOrderStatusEnum.CANCELLED).event(PayinOrderPayEventsEnum.ORDER_CANCEL)
                
                //终态
                //.and().withExternal()
                //.source(PayinOrderStatusEnum.DEPOSIT_COMPLETED).target(PayinOrderStatusEnum.SUCCEEDED).event(PayinOrderPayEventsEnum.ALL_SUCCESS)
                //.and().withExternal()
                //.source(PayinOrderStatusEnum.CONVERT_COMPLETED).target(PayinOrderStatusEnum.SUCCEEDED).event(PayinOrderPayEventsEnum.ALL_SUCCESS)

        ;
    }
}
