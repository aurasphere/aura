package co.aurasphere.aura.common.util;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class SerializationFacility {

	private static final Logger logger = LoggerFactory
			.getLogger(SerializationFacility.class);

	public String toJsonString(Object object) {
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = null;
		try {
			jsonString = mapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			logger.error("Error during serialization of object " + object, e);
		}
		return jsonString;
	}
	
	public <T> T fromJsonToObject(String request, Class<T> klass) {
		ObjectMapper mapper = new ObjectMapper();
		T object = null;
		try {
			object = mapper.readValue(request, klass);
		} catch (IOException e) {
			logger.error("Error during deserialization of String " + request, e);
		}
		return object;
	}

}
