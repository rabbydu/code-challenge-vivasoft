package com.rabbydu.notificationservice.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebsocketController {

	@MessageMapping("/websocket")
	@SendTo("/topic/outgoing") // test controller
	public String incoming(String message) {
		System.out.println(message);
		return message;
	}
}
