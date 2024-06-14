package com.demo.bbq.commons.errors.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import okhttp3.ResponseBody;

public class ErrorSerializerUtil {

  private ErrorSerializerUtil() {}

  private static final ObjectMapper objectMapper = new ObjectMapper();
  private static final Gson gson = new Gson();

  public static <T> byte[] serializeObject(T object, String defaultMessage) {
    try {
      return objectMapper.writeValueAsBytes(object);
    } catch (JsonProcessingException e) {
      return defaultMessage.getBytes(StandardCharsets.UTF_8);
    }
  }

  public static <T> T deserializeError(ResponseBody responseBody, Class<T> objectClass) {
    try {
      String stringJson = responseBody.string();
      return gson.fromJson(stringJson, objectClass);
    } catch (IOException ex) {
      return null;
    }
  }

  @SuppressWarnings("unchecked")
  public static <E extends Throwable> void propagate(Throwable exception) throws E {
    throw (E) exception;
  }
}
