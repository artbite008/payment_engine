package com.siupay.message.producer.client;

public interface SiuPayKafkaClient {
    void send(String topic, Object data);
}
