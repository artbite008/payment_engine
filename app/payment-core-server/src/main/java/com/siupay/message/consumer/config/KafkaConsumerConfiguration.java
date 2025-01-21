package com.siupay.message.consumer.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

@Configuration
@EnableConfigurationProperties(value = KafkaConsumerProperties.class)
public class KafkaConsumerConfiguration {

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerContainerFactory(KafkaConsumerProperties kafkaConsumerProperties) {

        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<String, String>();
        factory.setConsumerFactory(consumerFactory(kafkaConsumerProperties));
        factory.setConcurrency(4);
        factory.getContainerProperties().setPollTimeout(4000);

        return factory;
    }

    public ConsumerFactory<String, String> consumerFactory(KafkaConsumerProperties kafkaConsumerProperties) {
        Map<String, Object> properties = new HashMap<String, Object>();
        Map<String, String> props = kafkaConsumerProperties.getProps();
        props.entrySet().forEach(entry -> properties.put(entry.getKey(), entry.getValue()));

        properties.putIfAbsent(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConsumerProperties.getServer());
        properties.putIfAbsent(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        properties.putIfAbsent(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "100");
        properties.putIfAbsent(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "15000");
        properties.putIfAbsent(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        properties.putIfAbsent(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        properties.putIfAbsent(ConsumerConfig.GROUP_ID_CONFIG, kafkaConsumerProperties.getGroup());
        properties.putIfAbsent(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");

        return new DefaultKafkaConsumerFactory<String, String>(properties);
    }
}
