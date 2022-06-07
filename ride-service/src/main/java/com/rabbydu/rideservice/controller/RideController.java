package com.rabbydu.rideservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rabbydu.rideservice.dto.AcceptRideRequestDTO;
import com.rabbydu.rideservice.dto.BaseResponseDTO;
import com.rabbydu.rideservice.dto.PingRouteRequest;
import com.rabbydu.rideservice.dto.RideRequest;
import com.rabbydu.rideservice.dto.UserInfoDTO;
import com.rabbydu.rideservice.proxy.CustomerServiceProxy;
import com.rabbydu.rideservice.proxy.DriverServiceProxy;
import com.rabbydu.rideservice.service.RideService;

@RestController
@RequestMapping("/ride-serivce")
public class RideController {

	@Autowired
	private RideService service;

	@Autowired
	private CustomerServiceProxy customerProxy;

	@Autowired
	private DriverServiceProxy driverProxy;

	@PostMapping("/update-user-info")
	public BaseResponseDTO updateInfo(@RequestBody UserInfoDTO requestBody) {

		BaseResponseDTO response;

		boolean result = service.updateInfo(requestBody);

		if (result) {
			response = new BaseResponseDTO(true, "Successfully updated");
		} else {
			response = new BaseResponseDTO(false, "Failed to updated");
		}

		return response;
	}

	@PostMapping("/ping-route")
	public BaseResponseDTO pingRoute(@RequestBody PingRouteRequest requestBody) {

		BaseResponseDTO response;

		service.pingRoute(requestBody);

		response = new BaseResponseDTO(true, "Successfully updated");

		return response;
	}

	@PostMapping("/request-ride")
	public BaseResponseDTO requestForRide(@RequestBody RideRequest requestDTO) {

		return customerProxy.requestForRide(requestDTO.getCustomerId());
	}

	@PostMapping("/accept-ride")
	public BaseResponseDTO acceptRideRequest(@RequestBody AcceptRideRequestDTO requestDTO) {

		return driverProxy.acceptRideRequest(requestDTO);
	}

	@GetMapping("/get-all-ride-request/{driverId}")
	public List<UserInfoDTO> getAllRideRequest(@PathVariable String driverId) {

		return driverProxy.getAllRideRequest(driverId);
	}

}
