package com.rabbydu.notificationservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.rabbydu.notificationservice.dto.NotificationDTO;

@Service
public class FirebaseMessagingService {

	private Logger logger = LoggerFactory.getLogger(FirebaseMessagingService.class);

	@Autowired
	private FirebaseMessaging firebaseMessaging;

	public String sendNotificationToTarget(NotificationDTO dto, String token) {

		try {
			Notification notification = Notification.builder().setTitle(dto.getSubject()).setBody(dto.getContent())
					.build();

			Message message = Message.builder().setToken(token).setNotification(notification).putAllData(dto.getData())
					.build();

			return firebaseMessaging.send(message);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return null;

	}

	public String sendNotificationToTopic(NotificationDTO dto, String token) {
		try {

			Notification notification = Notification.builder().setTitle(dto.getSubject()).setBody(dto.getContent())
					.build();

			Message message = Message.builder().setTopic(token).setNotification(notification).putAllData(dto.getData())
					.build();

			return firebaseMessaging.send(message);

		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return null;
	}

}
