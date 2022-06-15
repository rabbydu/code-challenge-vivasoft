package com.rabbydu.notificationservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.rabbydu.notificationservice.dto.NotificationDTO;
import com.rabbydu.notificationservice.util.JsonFormatter;

@Component
public class KafkaListenerService {

	private Logger logger = LoggerFactory.getLogger(KafkaListenerService.class);

//	@Autowired
//	private FirebaseMessagingService messagingService;

	@Autowired
	private SimpMessagingTemplate simpTemplate;

	@KafkaListener(topics = "notification-topic", groupId = "notification-service")
	public void listenNotification(String data) {

		logger.info("listener received -> " + data);

		try {

			NotificationDTO dto = JsonFormatter.INSTANCE.fromJson(data, NotificationDTO.class);

			logger.info("decode received -> " + JsonFormatter.INSTANCE.toJson(dto));

//			messagingService.sendNotificationToTarget(dto, dto.getToken());

			String destination = "/topic/notification/" + dto.getToken();

			simpTemplate.convertAndSend(destination, data);

		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

}
