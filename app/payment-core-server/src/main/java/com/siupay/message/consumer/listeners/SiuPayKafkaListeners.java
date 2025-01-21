package com.siupay.message.consumer.listeners;//package com.siupay.message.consumer.listeners;
//
//import lombok.extern.slf4j.Slf4j;
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Component;
//
//import java.util.Optional;
//
//@Slf4j
//@Component
//public class SiuPayKafkaListeners {
//
//    @KafkaListener(topics = {"OTC_THRID_PARTY_CALL_BACK"})
//    public void testListener(ConsumerRecord<?, ?> record) {
//
//        Optional<?> messages = Optional.ofNullable(record.value());
//
//        if (messages.isPresent()) {
//            Object msg = messages.get();
//            log.warn("this is the testTopic send message: {}", msg);
//        }
//    }
//}
