package com.rabbydu.rideservice.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.rabbydu.rideservice.dto.BaseResponseDTO;

@FeignClient(name = "customer-service")
public interface CustomerServiceProxy {

	@GetMapping("/customer-service/request-ride/{customerId}")
	public BaseResponseDTO requestForRide(@PathVariable String customerId);
}
