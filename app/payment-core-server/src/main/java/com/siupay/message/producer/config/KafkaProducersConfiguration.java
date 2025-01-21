package com.siupay.message.producer.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
@EnableConfigurationProperties(value = KafkaProducerProperties.class)
public class KafkaProducersConfiguration {


    public DefaultKafkaProducerFactory<String, Object> producerFactory(KafkaProducerProperties producerProperties) {
        Map<String, String> props = producerProperties.getProps();
        Map<String, Object> properties = new HashMap<>();
        props.entrySet().forEach(entry -> properties.put(entry.getKey(), entry.getValue()));

        properties.putIfAbsent(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, producerProperties.getServer());
        properties.putIfAbsent(ProducerConfig.BATCH_SIZE_CONFIG, "4096");
        properties.putIfAbsent(ProducerConfig.LINGER_MS_CONFIG, "1");
        properties.putIfAbsent(ProducerConfig.BUFFER_MEMORY_CONFIG, "40960");
        properties.putIfAbsent(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        properties.putIfAbsent(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

        return new DefaultKafkaProducerFactory<String, Object>(properties);
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate(KafkaProducerProperties producerProperties) {
        KafkaTemplate<String, Object> kafkaTemplate = new KafkaTemplate<String, Object>(producerFactory(producerProperties));
        return kafkaTemplate;
    }
}
