package com.rabbydu.customerservice.util;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonFormatter {

	private static ObjectMapper objectMapper = new ObjectMapper();
	private Logger logger = LoggerFactory.getLogger(JsonFormatter.class);

	public static JsonFormatter INSTANCE = new JsonFormatter();

	private JsonFormatter() {
		objectMapper.setSerializationInclusion(Include.NON_NULL);
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.configure(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature(), true);
	}

	public Map<String, String> objectToMap(Object obj) {
		Map<String, String> map = new HashMap<>();
		for (Field field : obj.getClass().getDeclaredFields()) {
			field.setAccessible(true);
			try {
				map.put(field.getName(), field.get(obj).toString());
			} catch (Exception e) {
			}
		}
		return map;
	}

	public String toJson(Object message) {
		try {
			String msg = this.objectMapper.writeValueAsString(message);
			return msg;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error to parse object to json", e);
		}
		return null;
	}

	public JsonNode toJsonNode(Object message) {
		try {

			return this.objectMapper.convertValue(message, JsonNode.class);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error to parse object to json", e);
		}
		return null;
	}

	public <T> T fromJson(String json, Class<T> clazz) {
		try {
			if (!StringUtils.isEmpty(json)) {
				return this.objectMapper.readValue(json, clazz);
			}

		} catch (Exception e) {
			logger.error("error to parse json to object", e);
		}

		return null;
	}

	public <T> T convertValue(Map<String, String> json, Class<T> clazz) {
		try {

			return this.objectMapper.convertValue(json, clazz);
		} catch (Exception e) {
			logger.error("error to parse json to object", e);
		}

		return null;
	}

	public <T> T convertValue(Object json, Class<T> clazz) {
		try {

			return this.objectMapper.convertValue(json, clazz);
		} catch (Exception e) {
			logger.error("error to parse json to object", e);
		}

		return null;
	}

	public JsonNode valueToTree(Object json) {
		try {
			return objectMapper.valueToTree(json);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error to parse json to object", e);
		}

		return null;
	}

	public JsonNode fromJsonObject(JSONObject json) {
		try {
			return objectMapper.readTree(json.toString());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error to parse json to object", e);
		}

		return null;
	}

	public JsonNode fromJsonString(String jsonStr) {
		try {
			return objectMapper.readTree(jsonStr);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error to parse json to object", e);
		}

		return null;
	}

	public <T> T fromJson(File json, Class<T> clazz) {
		try {
			return this.objectMapper.readValue(json, clazz);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error to parse json to object", e);
		}

		return null;
	}

	public <T> T[] fromJsonArray(String json, Class<T[]> clazz) {
		try {
			return this.objectMapper.readValue(json, clazz);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error to parse json to object", e);
		}

		return null;
	}

	public <T> List<T> fromJsonList(String json, Class<T[]> clazz) {
		try {
			return Arrays.asList(this.objectMapper.readValue(json, clazz));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("error to parse json to object", e);
		}

		return null;
	}
}
