package com.rabbydu.customerservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisPoolConfig {

	@Value("${spring.redis.host}")
	String redisHost;

	@Value("${spring.redis.port}")
	int port;

	@Value("${spring.redis.password}")
	String password;

	@Bean
	public PoolConfigurer redisConnectionFactory() {
		PoolConfigurer pool = new PoolConfigurer();
		pool.configure(password, redisHost, port);
		return pool;
	}
}
