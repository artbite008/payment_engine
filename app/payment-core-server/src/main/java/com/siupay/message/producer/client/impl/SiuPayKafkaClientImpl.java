package com.siupay.message.producer.client.impl;

import com.siupay.message.producer.client.SiuPayKafkaClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;

@Component
public class SiuPayKafkaClientImpl implements SiuPayKafkaClient {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void send(String topic, Object data) {
        kafkaTemplate.send(topic, JSON.toJSONString(data));
    }
}
