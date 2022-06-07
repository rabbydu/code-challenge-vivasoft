package com.rabbydu.driverservice.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.rabbydu.driverservice.dto.AcceptRideRequestDTO;
import com.rabbydu.driverservice.dto.BaseResponseDTO;
import com.rabbydu.driverservice.dto.NotificationDTO;
import com.rabbydu.driverservice.dto.UserInfoDTO;
import com.rabbydu.driverservice.util.Constants;
import com.rabbydu.driverservice.util.JsonFormatter;
import com.rabbydu.driverservice.util.RedisUtil;

@Service
public class DriverService {

	private Logger logger = LoggerFactory.getLogger(DriverService.class);

	@Value("${spring.kafka.topic-name.notification}")
	private String notificationTopic;

	@Autowired
	private RedisUtil redisUtil;

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	public BaseResponseDTO acceptRide(AcceptRideRequestDTO dto) {

		BaseResponseDTO response = new BaseResponseDTO();

		try {

			logger.info(JsonFormatter.INSTANCE.toJson(dto));

			UserInfoDTO customerInfo = JsonFormatter.INSTANCE
					.fromJson(redisUtil.hget(Constants.KEY_ALL_CUSTOMER, dto.getCustomerId()), UserInfoDTO.class);

			UserInfoDTO driverInfo = JsonFormatter.INSTANCE
					.fromJson(redisUtil.hget(Constants.KEY_ALL_DRIVER, dto.getDriverId()), UserInfoDTO.class);

			String customerRouteId = redisUtil.get(Constants.KEY_CUSTOMER_ROUTE_PREFIX + dto.getCustomerId());

			if (redisUtil.srem(Constants.KEY_REQUEST_FOR_RIDE_PREFIX + customerRouteId, dto.getCustomerId()) == 0) {
				response.setSuccess(false);
				response.setMessage("Ride request is not available for the customer");
				return response;
			}

			Set<String> availableDriverList = redisUtil
					.smembers(Constants.KEY_AVAILABLE_DRIVER_PREFIX + customerRouteId);

			if (!availableDriverList.contains(driverInfo.getId())) {
				redisUtil.sadd(Constants.KEY_REQUEST_FOR_RIDE_PREFIX + customerRouteId, dto.getCustomerId());
				response.setSuccess(false);
				response.setMessage("No driver availabe to server this ride");
				return response;
			}

			// send notification to other driver
			availableDriverList.stream().filter(driverId -> !driverId.equals(dto.getDriverId())).forEach(driverId -> {

				UserInfoDTO driverDTO = JsonFormatter.INSTANCE
						.fromJson(redisUtil.hget(Constants.KEY_ALL_DRIVER, driverId), UserInfoDTO.class);

				NotificationDTO notificationDTO = new NotificationDTO();
				notificationDTO.setSubject("Ride Request");
				notificationDTO.setContent("Ride Request already accepted by other driver");
				notificationDTO.setToken(driverDTO.getFirebaseToken());
				Map<String, String> data = JsonFormatter.INSTANCE.convertValue(customerInfo, Map.class);
				data.put("type", "ride-request-already-accepted");
				notificationDTO.setData(data);
				kafkaTemplate.send(notificationTopic, JsonFormatter.INSTANCE.toJson(notificationDTO));

				logger.info(JsonFormatter.INSTANCE.toJson(notificationDTO));

			});

			// send notification to customer
			NotificationDTO customerNotificationDTO = new NotificationDTO();
			customerNotificationDTO.setSubject("Ride Request");
			customerNotificationDTO.setContent("Ride Request has been accepted");
			customerNotificationDTO.setToken(customerInfo.getFirebaseToken());
			Map<String, String> data = JsonFormatter.INSTANCE.convertValue(driverInfo, Map.class);
			data.put("type", "ride-request-accepted-by-driver");
			customerNotificationDTO.setData(data);
			kafkaTemplate.send(notificationTopic, JsonFormatter.INSTANCE.toJson(customerNotificationDTO));

			response.setSuccess(true);
			response.setMessage("Rider request accept successfully");

		} catch (Exception e) {
			logger.error(e.getMessage());
			response.setSuccess(false);
			response.setMessage(e.getMessage());
		}

		return response;
	}

	public Set<String> getAllAvailableDriverOfRoute(String routId) {

		Set<String> availableDriverList = new HashSet<String>();

		try {

			availableDriverList = redisUtil.smembers(Constants.KEY_AVAILABLE_DRIVER_PREFIX + routId);

		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return availableDriverList;
	}

	public List<UserInfoDTO> getAllRideRequest(String driverId) {

		List<UserInfoDTO> allCustomer = new ArrayList<UserInfoDTO>();

		try {

			UserInfoDTO driverInfo = JsonFormatter.INSTANCE.fromJson(redisUtil.hget(Constants.KEY_ALL_DRIVER, driverId),
					UserInfoDTO.class);

			if (driverInfo == null) {
				return allCustomer;
			}

			String driverRoute = redisUtil.get(Constants.KEY_DRIVER_ROUTE_PREFIX + driverId);

			Set<String> allRideRequest = redisUtil.smembers(Constants.KEY_REQUEST_FOR_RIDE_PREFIX + driverRoute);

			allRideRequest.forEach(customerId -> {

				UserInfoDTO customerInfo = JsonFormatter.INSTANCE
						.fromJson(redisUtil.hget(Constants.KEY_ALL_CUSTOMER, customerId), UserInfoDTO.class);

				allCustomer.add(customerInfo);
			});

		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return allCustomer;
	}

}
