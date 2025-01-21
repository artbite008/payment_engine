package com.siupay.message.consumer.listeners;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.siupay.channel.core.bo.PayInOrderHistorySyncEventBo;
import com.siupay.channel.core.dto.record.PayinResultEventBo;
import com.siupay.common.lib.enums.KafkaEventType;
import com.siupay.common.lib.event.IEvent;
import com.siupay.common.lib.json.JsonUtils;
import com.siupay.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class ChannelCorePayinListener {

    @Autowired
    private PaymentService paymentService;

    @KafkaListener(topics = {"CHANNEL_CORE_PAYIN_CALL_BACK_EVENT"})
    public void payinListener(ConsumerRecord<?, ?> record) throws Exception {

        Optional<?> messages = Optional.ofNullable(record.value());
        if (messages.isPresent()) {
            IEvent iEvent = JSON.parseObject(String.valueOf(messages.get()), IEvent.class);
            log.info("channel core kafka notify msg is {}", JsonUtils.toJson(iEvent));
            if (KafkaEventType.CHANNEL_CORE_PAYIN_CALL_BACK_EVENT.getEventTypeId()== iEvent.getEventType()){
                IEvent<PayinResultEventBo> event = JSON.parseObject(String.valueOf(messages.get()),
                        new TypeReference<IEvent<PayinResultEventBo>>() {});
                paymentService.payinAsyncProcess(event);
            }
        }else {
            log.error("channel core kafka notify msg is null");
        }
    }

    @KafkaListener(topics = {"CHANNEL_CORE_PAYIN_ORDER_SYNC_EVENT"})
    public void payinOrderSyncListener(ConsumerRecord<?, ?> record) {
        Optional<?> messages = Optional.ofNullable(record.value());
        if (messages.isPresent()) {
            IEvent iEvent = JSON.parseObject(String.valueOf(messages.get()), IEvent.class);
            log.info("[ChannelCorePayinListener.payinOrderSyncListener]channel core kafka msg is {}", JsonUtils.toJson(iEvent));
            if (KafkaEventType.CHANNEL_CORE_PAYIN_ORDER_SYNC_EVENT.getEventTypeId()== iEvent.getEventType()){
                IEvent<PayInOrderHistorySyncEventBo> event = JSON.parseObject(String.valueOf(messages.get()),
                        new TypeReference<IEvent<PayInOrderHistorySyncEventBo>>() {});
                paymentService.payinOrderHistorySyncProcess(event);
            }
        }else {
            log.error("[ChannelCorePayinListener.payinOrderSyncListener] channel core kafka notify msg is null");
        }
    }

}
