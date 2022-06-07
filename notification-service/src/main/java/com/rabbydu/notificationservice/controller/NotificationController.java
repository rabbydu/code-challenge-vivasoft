package com.rabbydu.notificationservice.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.rabbydu.notificationservice.dto.NotificationDTO;
import com.rabbydu.notificationservice.service.FirebaseMessagingService;

@RestController
public class NotificationController {
	
	@Autowired
	private FirebaseMessagingService service;
	
	@GetMapping("/send/{token}")
	public String setNotification(@PathVariable String token) {
		
		Map<String, String> data = new HashMap<String, String>();
		data.put("token", token);
		
		NotificationDTO dto = new NotificationDTO();
		dto.setSubject("Subject");
		dto.setContent("Message content");
		dto.setData(data);
		
		
		service.sendNotificationToTarget(dto, token);
		return "ok";
	}

}
