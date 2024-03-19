package com.demo.bbq.support.util;

import com.demo.bbq.support.exception.model.ApiException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class JsonFileReader {

  private static final ObjectMapper objectMapper = new ObjectMapper();

  public static <T> T getAnElement(String filePath, Class<T> classSource) {
    try {
      return objectMapper.readValue(JsonFileReader.class.getClassLoader().getResourceAsStream(filePath), classSource);
    } catch (IOException ioException) {
      throw buildException.apply(ioException);
    }
  }

  public static <T> List<T> getList(String filePath, Class<T[]> classSource) {
    try {
      T[] array = objectMapper.readValue(Objects.requireNonNull(JsonFileReader.class.getClassLoader().getResourceAsStream(filePath)), classSource);
      return Arrays.asList(array);
    } catch (IOException ioException) {
      throw buildException.apply(ioException);
    }
  }

  private static final Function<IOException, ApiException> buildException = ioException ->
      ApiException.builder()
          .cause(ioException)
          .message("Error reading JSON file")
          .build();
}