package com.siupay.message.producer.config;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "payment.kafka.producer")
public class KafkaProducerProperties  {

    @Value("${payment.kafka.producer.props.bootstrap.server}")
    private String server;

    private Map<String,String> props;
}
