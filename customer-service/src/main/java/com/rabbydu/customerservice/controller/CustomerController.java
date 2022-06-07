package com.rabbydu.customerservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rabbydu.customerservice.dto.BaseResponseDTO;
import com.rabbydu.customerservice.service.CustomerService;

@RestController
@RequestMapping("/customer-service")
public class CustomerController {

	@Autowired
	private CustomerService service;

	@GetMapping("/request-ride/{customerId}")
	public BaseResponseDTO requestForRide(@PathVariable String customerId) {

		return service.requestForRide(customerId);
	}
}
