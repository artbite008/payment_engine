package com.siupay.message.consumer.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "payment.kafka.consumer")
public class KafkaConsumerProperties {

    @Value("${payment.kafka.consumer.props.bootstrap.server}")
    private String server;

    private String group;

    private Map<String,String> props = new HashMap<>();
}
