package com.rabbydu.notificationservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.rabbydu.notificationservice.dto.NotificationDTO;
import com.rabbydu.notificationservice.util.JsonFormatter;

@Component
public class KafkaListenerService {

	private Logger logger = LoggerFactory.getLogger(KafkaListenerService.class);

	@Value("${spring.kafka.topic-name.notification}")
	private String notificationTopic;

	@Autowired
	private FirebaseMessagingService messagingService;

	@KafkaListener(topics = "notification-topic", groupId = "notification-service")
	void listenNotification(String data) {

		logger.info("listener received -> " + data);
		
		try {
			
			NotificationDTO dto = JsonFormatter.INSTANCE.fromJson(data, NotificationDTO.class);
			
			logger.info("decode received -> " + JsonFormatter.INSTANCE.toJson(dto));

			messagingService.sendNotificationToTarget(dto, dto.getToken());
			
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
}
