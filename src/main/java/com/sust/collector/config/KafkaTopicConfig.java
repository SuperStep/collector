package com.sust.collector.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {

//    @Value(value = "${spring.kafka.producer.bootstrap-servers}")
//    private String bootstrapAddress;


    @Bean
    public NewTopic topic1() {
        return new NewTopic("articles", 1, (short) 1);
    }
}