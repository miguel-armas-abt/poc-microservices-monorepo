package com.demo.bbq.commons.errors.serializer;

import com.demo.bbq.commons.errors.dto.ErrorDTO;
import com.demo.bbq.commons.errors.exceptions.ExternalServiceException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.springframework.web.client.HttpStatusCodeException;

public class ErrorSerializerUtil {

  private ErrorSerializerUtil() {}

  private static final ObjectMapper objectMapper = new ObjectMapper();

  public static <T> byte[] serializeObject(T object, String defaultMessage) {
    try {
      return objectMapper.writeValueAsBytes(object);
    } catch (JsonProcessingException e) {
      return defaultMessage.getBytes(StandardCharsets.UTF_8);
    }
  }

  public static <T> T deserializeError(Throwable cause, Class<T> objectClass) {
    if((cause instanceof ExternalServiceException exception) && objectClass.isAssignableFrom(ErrorDTO.class)) {
      return (T) exception.getErrorDetail();
    }

    if(cause instanceof HttpStatusCodeException exception) {
      return deserializeError(exception, objectClass);
    }

    propagate(cause);
    return null;
  }

  private static <T> T deserializeError(HttpStatusCodeException exception, Class<T> objectClass) {
    try {
      byte[] bytesJSONResponse = exception.getResponseBodyAsByteArray();
      return objectMapper
          .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true)
          .readValue(bytesJSONResponse, objectClass);
    } catch (IOException ex) {
      return null;
    }
  }

  @SuppressWarnings("unchecked")
  private static <E extends Throwable> void propagate(Throwable exception) throws E {
    throw (E) exception;
  }
}
