package com.rabbydu.customerservice.proxy;

import java.util.Set;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "driver-service")
public interface DriverServiceProxy {

	@GetMapping("/driver-service/available-drivers/{routeId}")
	public Set<String> getAllAvailableDrivers(@PathVariable String routeId);
}
