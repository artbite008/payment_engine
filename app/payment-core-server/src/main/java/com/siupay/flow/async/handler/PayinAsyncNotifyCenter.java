package com.siupay.flow.async.handler;

import javax.annotation.Resource;

import com.siupay.core.dal.mybatis.entity.PayinOrderEntity;
import com.siupay.core.dal.mybatis.repositry.PayinOrderRepository;
import com.siupay.enums.PayinOrderStatusEnum;
import com.siupay.flow.async.PayinAsyncContext;
import com.siupay.manager.PdcMsgService;
import com.siupay.utils.notice.impl.NoticeHelperImpl;
import com.siupay.utils.sensors.SensorsTrackingUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PayinAsyncNotifyCenter implements Command {

    @Autowired
    private PayinOrderRepository payinOrderRepository;

    @Autowired
    private NoticeHelperImpl noticeHelper;

    @Autowired
    private SensorsTrackingUtils sensorsTrackingUtils;

    @Resource
    private PdcMsgService pdcMsgService;

    @Override
    public boolean execute(Context context) throws Exception {
        PayinAsyncContext payinAsyncContext = (PayinAsyncContext) context;
        PayinOrderEntity payinOrder = payinOrderRepository.getById(payinAsyncContext.getPayinOrderId());

        pdcMsgService.collect(payinOrder);
        PayinOrderStatusEnum payinOrderStatusEnum = PayinOrderStatusEnum.valueOf(payinOrder.getStatus());
        if (payinOrderStatusEnum.isFinalState()) {
            log.info("{}订单状态终态，无须发送用户消息", payinOrder.getPayinOrderId());
//            noticeHelper.sendPayinOrderNotify(payinOrder);
//            sensorsTrackingUtils.sendWhenOrderCompleted(payinOrder);
        } else {
            log.info("订单状态为[{}]非终态，无须发送用户消息", payinOrderStatusEnum);
        }

        return false;
    }

}
