package com.siupay.flow.confirm.handler;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.siupay.common.lib.utils.AssertUtils;
import com.siupay.config.state.PayinStateMachineService;
import com.siupay.flow.confirm.PayinOrderConfirmContext;
import com.siupay.core.dal.mybatis.entity.PayinOrderEntity;
import com.siupay.core.dal.mybatis.repositry.PayinOrderRepository;
import com.siupay.core.dto.PayinOrderConfirmRequest;
import com.siupay.core.dto.PayinOrderConfirmResponse;
import com.siupay.enums.PayinOrderPayEventsEnum;
import com.siupay.enums.PayinOrderStatusEnum;
import com.siupay.utils.BaseFacade;
import com.siupay.utils.sensors.SensorsTrackingUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Objects;

@Component
@Slf4j
public class ConfirmPayinOrderUpdateAndReturn extends BaseFacade implements Command {

    @Autowired
    private PayinStateMachineService payinStateMachine;
    @Autowired
    private PayinOrderRepository payinOrderRepository;

    @Override
    public boolean execute(Context context) throws Exception {
        PayinOrderConfirmRequest request = ((PayinOrderConfirmContext) context).getRequest();
        PayinOrderEntity payinOrder = payinOrderRepository.getById(request.getOrderId());
        updatePayinOrder(context,payinOrder);
        buildResponse(context,payinOrder);
        return false;
    }

    private void updatePayinOrder(Context context,PayinOrderEntity payinOrder){
        PayinOrderConfirmContext confirmContext = (PayinOrderConfirmContext) context;
        String event = confirmContext.getChannelCoreStatus().toUpperCase();
        //同步返回AUTHORIZED,代表2D结果成功，3D/2D统一通过异步通知拿最终结果
        //if (PayinOrderPayEventsEnum.AUTHORIZED.name().equals(confirmContext.getChannelCoreStatus())) {
        //    event = PayinOrderPayEventsEnum.PENDING_PROCESSING.name();
        //}
        PayinOrderPayEventsEnum eventsEnum = PayinOrderPayEventsEnum.valueOf(event);
        payinOrder.setChannelCoreId(confirmContext.getChannelCoreId());
        payinStateMachine.sentEvent(payinOrder,eventsEnum);
        if (payinOrder.getStatus().equals(PayinOrderStatusEnum.CUSTOMER_ACTION.name())) {
            payinOrder.setNextAction(confirmContext.getRedirectUrl());
            //发生3D上报神策
//            sensorsTrackingUtils.sendWhenThreeDS(payinOrder);
        }

        //只有数据库状态是CREATE状态才更新
        AssertUtils.notNull(payinOrder.getPayinOrderId(),"payin order id not null");
        LambdaUpdateWrapper<PayinOrderEntity> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(PayinOrderEntity::getStatus,PayinOrderStatusEnum.CREATED.name());
        updateWrapper.eq(PayinOrderEntity::getPayinOrderId,payinOrder.getPayinOrderId());
        payinOrderRepository.update(payinOrder,updateWrapper);
    }

    private void buildResponse(Context context,PayinOrderEntity payinOrder){
        PayinOrderConfirmResponse response = ((PayinOrderConfirmContext) context).getResponse();
        response.setOrderId(payinOrder.getPayinOrderId());
        response.setStatus(payinOrder.getStatus());
        //同步失败也可能会有redirectUrl
        String redirectUrl = ((PayinOrderConfirmContext) context).getRedirectUrl();
        PayinOrderConfirmResponse.NextAction nextAction = new PayinOrderConfirmResponse.NextAction();
        if (!StringUtils.isEmpty(redirectUrl)){
            nextAction.setRedirectUrl(redirectUrl);
        }
        Map<String, Object> apmInfo = ((PayinOrderConfirmContext) context).getApmInfo();
        if (!Objects.nonNull(apmInfo)){
            nextAction.setApmInfo(apmInfo);
        }
        response.setNextAction(nextAction);
        ((PayinOrderConfirmContext) context).setResponse(response);
    }

}
