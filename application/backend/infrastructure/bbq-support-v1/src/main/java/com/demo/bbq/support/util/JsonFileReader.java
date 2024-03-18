package com.demo.bbq.support.util;

import com.demo.bbq.support.exception.model.ApiException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.function.Function;

public class JsonFileReader<T> {

  private final ObjectMapper objectMapper = new ObjectMapper();
  private final Class<T> type;

  public JsonFileReader(Class<T> type) {
    this.type = type;
  }

  public T readObjectFromFile(String filePath) {
    try {
      InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath);
      return objectMapper.readValue(inputStream, type);
    } catch (IOException ioException) {
      throw buildException.apply(ioException);
    }
  }

  public List<T> readListFromFile(String filePath) throws IOException {
    try {
      InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath);
      TypeReference<List<T>> typeReference = new TypeReference<List<T>>() {};
      return objectMapper.readValue(inputStream, typeReference);
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
