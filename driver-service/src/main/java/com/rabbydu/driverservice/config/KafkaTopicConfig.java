package com.rabbydu.driverservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {
	
	@Value("${spring.kafka.topic-name.notification}")
	private String notificationTopic;

	@Bean
	public NewTopic notificationTopic() {
		return TopicBuilder.name(notificationTopic).build();
	}

}
