package com.rabbydu.rideservice.service;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rabbydu.rideservice.dto.PingRouteRequest;
import com.rabbydu.rideservice.dto.UserInfoDTO;
import com.rabbydu.rideservice.util.Constants;
import com.rabbydu.rideservice.util.JsonFormatter;
import com.rabbydu.rideservice.util.RedisUtil;

@Service
public class RideService {

	private static Logger logger = LoggerFactory.getLogger(RideService.class);

	@Autowired
	private RedisUtil redisUtil;

	public boolean updateInfo(UserInfoDTO dto) {

		try {
			switch (dto.getUserType()) {
			case Constants.USER_TYPE_CUSTOMER:
				redisUtil.hset(Constants.KEY_ALL_CUSTOMER, dto.getId(), JsonFormatter.INSTANCE.toJson(dto));
				break;

			case Constants.USER_TYPE_DRIVER:
				redisUtil.hset(Constants.KEY_ALL_DRIVER, dto.getId(), JsonFormatter.INSTANCE.toJson(dto));
				break;
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}

		return true;
	}

	public void pingRoute(PingRouteRequest dto) {

		try {
			
			logger.info(JsonFormatter.INSTANCE.toJson(dto));
			
			switch (dto.getUserType()) {

			case Constants.USER_TYPE_CUSTOMER: {

				String preRoute = redisUtil.get(Constants.KEY_CUSTOMER_ROUTE_PREFIX + dto.getId());
				
				logger.info(preRoute);

				if (StringUtils.isEmpty(preRoute) || !preRoute.equals(dto.getRouteId())) {

					redisUtil.set(Constants.KEY_CUSTOMER_ROUTE_PREFIX + dto.getId(), dto.getRouteId());
					redisUtil.srem(Constants.KEY_REQUEST_FOR_RIDE_PREFIX + preRoute, dto.getId());
					
					//notify all driver for cancel ride request
				}

				break;
			}

			case Constants.USER_TYPE_DRIVER: {

				String preRoute = redisUtil.get(Constants.KEY_DRIVER_ROUTE_PREFIX + dto.getId());

				if (StringUtils.isEmpty(preRoute) || !preRoute.equals(dto.getRouteId())) {

					redisUtil.set(Constants.KEY_DRIVER_ROUTE_PREFIX + dto.getId(), dto.getRouteId());

					redisUtil.sadd(Constants.KEY_AVAILABLE_DRIVER_PREFIX + dto.getRouteId(), dto.getId());
					redisUtil.srem(Constants.KEY_AVAILABLE_DRIVER_PREFIX + preRoute, dto.getId());
				}

				break;
			}

			}

		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
}
