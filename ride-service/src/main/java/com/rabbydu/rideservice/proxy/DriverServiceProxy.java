package com.rabbydu.rideservice.proxy;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


import com.rabbydu.rideservice.dto.AcceptRideRequestDTO;
import com.rabbydu.rideservice.dto.BaseResponseDTO;
import com.rabbydu.rideservice.dto.UserInfoDTO;

@FeignClient(name = "driver-service")
public interface DriverServiceProxy {

	@PostMapping("/driver-service/accept-ride")
	public BaseResponseDTO acceptRideRequest(@RequestBody AcceptRideRequestDTO requestDTO);
	
	@GetMapping("/driver-service/get-all-ride-request/{driverId}")
	public List<UserInfoDTO> getAllRideRequest(@PathVariable String driverId);

}
