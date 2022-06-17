package com.rabbydu.notificationservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		
		// rabbitmq stomp broker plugin. Rabbitmq required
		config.enableStompBrokerRelay("/topic")
			.setRelayHost("localhost")
			.setRelayPort(61613)
			.setSystemLogin("guest")
			.setSystemPasscode("guest");

		// kafka don't have plugin
//		config.enableSimpleBroker("/topic");
		
		config.setApplicationDestinationPrefixes("/app");
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/websocket").setAllowedOriginPatterns("*").withSockJS();
	}

}
