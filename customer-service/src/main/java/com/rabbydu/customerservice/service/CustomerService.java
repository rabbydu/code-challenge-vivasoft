package com.rabbydu.customerservice.service;

import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.rabbydu.customerservice.dto.BaseResponseDTO;
import com.rabbydu.customerservice.dto.NotificationDTO;
import com.rabbydu.customerservice.dto.UserInfoDTO;
import com.rabbydu.customerservice.proxy.DriverServiceProxy;
import com.rabbydu.customerservice.util.Constants;
import com.rabbydu.customerservice.util.JsonFormatter;
import com.rabbydu.customerservice.util.RedisUtil;

@Service
public class CustomerService {

	private static Logger logger = LoggerFactory.getLogger(CustomerService.class);

	@Value("${spring.kafka.topic-name.notification}")
	private String notificationTopic;

	@Autowired
	private RedisUtil redisUtil;

	@Autowired
	private DriverServiceProxy driverServiceProxy;

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	public BaseResponseDTO requestForRide(String customerId) {

		BaseResponseDTO response = new BaseResponseDTO();

		try {

			logger.info(customerId);

			UserInfoDTO customerDTO = JsonFormatter.INSTANCE
					.fromJson(redisUtil.hget(Constants.KEY_ALL_CUSTOMER, customerId), UserInfoDTO.class);

			if (customerDTO == null) {
				response.setSuccess(false);
				response.setMessage("Customer is not available");
				return response;
			}

			logger.info(JsonFormatter.INSTANCE.toJson(customerDTO));

			String customerRouteId = redisUtil.get(Constants.KEY_CUSTOMER_ROUTE_PREFIX + customerDTO.getId());

			logger.info(customerRouteId);

			Set<String> availableDriverList = driverServiceProxy.getAllAvailableDrivers(customerRouteId);

			if (availableDriverList.size() == 0) {
				response.setSuccess(false);
				response.setMessage("No driver available in this route");
				return response;
			}

			// send ride request notification to all driver
			availableDriverList.forEach(driverId -> {

				UserInfoDTO dto = JsonFormatter.INSTANCE.fromJson(redisUtil.hget(Constants.KEY_ALL_DRIVER, driverId),
						UserInfoDTO.class);

				NotificationDTO notificationDTO = new NotificationDTO();
				notificationDTO.setSubject("Ride Request");
				notificationDTO.setContent("Ride Request sent from customer");
				notificationDTO.setToken(dto.getFirebaseToken());
				Map<String, String> data = JsonFormatter.INSTANCE.convertValue(customerDTO, Map.class);
				data.put("type", "request-for-ride");
				notificationDTO.setData(data);
				logger.info(JsonFormatter.INSTANCE.toJson(notificationDTO));
				kafkaTemplate.send(notificationTopic, JsonFormatter.INSTANCE.toJson(notificationDTO));

				logger.info(JsonFormatter.INSTANCE.toJson(notificationDTO));
			});

			redisUtil.sadd(Constants.KEY_REQUEST_FOR_RIDE_PREFIX + customerRouteId, customerId);

			response.setSuccess(true);
			response.setMessage("Ride Request send successfully");

		} catch (Exception e) {
			logger.error(e.getMessage());
			response.setSuccess(false);
			response.setMessage(e.getMessage());
		}

		return response;
	}
}
