package com.siupay.flow.async.handler;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.siupay.channel.core.dto.record.PayinResultEventBo;
import com.siupay.common.api.exception.ErrorCode;
import com.siupay.common.api.exception.PaymentException;
import com.siupay.config.state.PayinStateMachineService;
import com.siupay.core.dal.mybatis.entity.PayinOrderEntity;
import com.siupay.core.dal.mybatis.repositry.PayinOrderRepository;
import com.siupay.enums.PayinOrderPayEventsEnum;
import com.siupay.enums.PayinOrderStatusEnum;
import com.siupay.flow.async.PayinAsyncContext;
import com.siupay.utils.sensors.SensorsTrackingUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Slf4j
public class PayinAsyncValidate implements Command {

    @Autowired
    private PayinOrderRepository payinOrderRepository;
    @Autowired
    private PayinStateMachineService payinStateMachine;
    @Autowired
    private SensorsTrackingUtils sensorsTrackingUtils;
    @Autowired
    private PayinAsyncNotifyCenter payinAsyncNotifyCenter;

    @Override
    public boolean execute(Context context) throws Exception {
        PayinAsyncContext payinAsyncContext = (PayinAsyncContext) context;
        PayinResultEventBo createResponse = payinAsyncContext.getPayinCreateResponse();
        PayinOrderPayEventsEnum eventsEnum = PayinOrderPayEventsEnum.valueOf(createResponse.getStatus().toUpperCase());
        if (!needProcessing(eventsEnum)) {
            return true;
        }

        //这里的request id就是payin orderId
        String payinOrderId = createResponse.getRequestId();
        LambdaQueryWrapper<PayinOrderEntity> lq = Wrappers.lambdaQuery();
        lq.eq(PayinOrderEntity::getPayinOrderId, payinOrderId);
        PayinOrderEntity payinOrder = payinOrderRepository.getOne(lq);
        if (!checkOrderNeedProcessing(payinOrder)) {
            return true;
        }

        String channelCoreId = createResponse.getPayinTrxnId();
        payinOrder.setChannelCoreId(channelCoreId);
        payinStateMachine.sentEvent(payinOrder, eventsEnum);
        payinOrderRepository.updateById(payinOrder);
        payinAsyncContext.setPayinOrderId(payinOrder.getPayinOrderId());

        //3D订单结果上报神策
        if (payinOrder.getRiskData().getNeed3DS()) {
            sensorsTrackingUtils.sendWhenThreeDSCompleted(payinOrder);
        }

        // 支付失败直接发消息
        if (isFailed(payinOrder)) {
            payinAsyncNotifyCenter.execute(context);
        }

        //channel-core异步通知支付失败,后续逻辑不走
        if (!isPaySuccess(payinOrder)) {
            return true;
        }

        return false;
    }

    private boolean checkOrderNeedProcessing(PayinOrderEntity payinOrder) {
        if (Objects.isNull(payinOrder)) {
            throw new PaymentException(ErrorCode.RECORD_NOT_EXIST, payinOrder.getPayinOrderId() + " 订单不存在");
        }

        PayinOrderStatusEnum payinOrderStatusEnum = PayinOrderStatusEnum.valueOf(payinOrder.getStatus());
        if (payinOrderStatusEnum.isFinalState()) {
            log.info("Payin orderId:{} of status:{} is finished", payinOrder.getPayinOrderId(), payinOrderStatusEnum);
            return false;
        }

        return true;
    }


    private boolean needProcessing(PayinOrderPayEventsEnum eventsEnum) {
        if (PayinOrderPayEventsEnum.PENDING == eventsEnum) {
            return false;
        }
        return true;
    }

    private boolean isPaySuccess(PayinOrderEntity payinOrder) {
        if (payinOrder.getStatus().equalsIgnoreCase(PayinOrderStatusEnum.PAYIN_COMPLETED.name())) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isFailed(PayinOrderEntity payinOrder) {
        if (payinOrder.getStatus().equalsIgnoreCase(PayinOrderStatusEnum.FAILED.name())) {
            return true;
        } else {
            return false;
        }
    }
}
