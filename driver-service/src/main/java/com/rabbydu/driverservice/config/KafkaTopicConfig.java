package com.rabbydu.driverservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import com.rabbydu.driverservice.util.Constants;

@Configuration
public class KafkaTopicConfig {

	@Bean
	public NewTopic notificationTopic() {
		return TopicBuilder.name(Constants.TOPIC_NOTIFICATION).build();
	}

}
