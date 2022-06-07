package com.rabbydu.driverservice.controller;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rabbydu.driverservice.dto.AcceptRideRequestDTO;
import com.rabbydu.driverservice.dto.BaseResponseDTO;
import com.rabbydu.driverservice.dto.UserInfoDTO;
import com.rabbydu.driverservice.service.DriverService;

@RestController
@RequestMapping("/driver-service")
public class DriverController {

	@Autowired
	private DriverService service;

	@PostMapping("/accept-ride")
	public BaseResponseDTO acceptRideRequest(@RequestBody AcceptRideRequestDTO requestDTO) {

		return service.acceptRide(requestDTO);
	}

	@GetMapping("/available-drivers/{routeId}")
	public Set<String> getAllAvailableDrivers(@PathVariable String routeId) {

		return service.getAllAvailableDriverOfRoute(routeId);
	}

	@GetMapping("/get-all-ride-request/{driverId}")
	public List<UserInfoDTO> getAllRideRequest(@PathVariable String driverId) {

		return service.getAllRideRequest(driverId);
	}
}
