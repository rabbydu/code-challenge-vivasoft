package com.rabbydu.notificationservice.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.rabbydu.notificationservice.dto.NotificationDTO;
import com.rabbydu.notificationservice.service.KafkaListenerService;
import com.rabbydu.notificationservice.util.JsonFormatter;

@RestController
public class NotificationController {

	@Autowired
	private KafkaListenerService service;

	@GetMapping("/send/{token}")
	public String setNotification(@PathVariable String token) {

		Map<String, String> data = new HashMap<String, String>();
		data.put("token", token);

		NotificationDTO dto = new NotificationDTO();
		dto.setSubject("Subject");
		dto.setContent("Message content");
		dto.setData(data);
		dto.setToken(token);

		service.listenNotification(JsonFormatter.INSTANCE.toJson(dto));
		return "ok";
	}

}
