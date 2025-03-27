package com.demo.poc.commons.core.serialization;

import com.demo.poc.commons.core.errors.exceptions.JsonReadException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static org.apache.commons.lang3.StringUtils.isNoneEmpty;

@Slf4j
@RequiredArgsConstructor
public class JsonSerializer {

  private final ObjectMapper objectMapper;

  @PostConstruct
  private void configureObjectMapper() {
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
  }

  public <T> T readElementFromFile(String filePath, Class<T> objectClass) {
    try {
      InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath);
      return objectMapper.readValue(inputStream, objectClass);
    } catch (IOException ex) {
      throw new JsonReadException(ex.getMessage());
    }
  }

  public <T> List<T> readListFromFile(String filePath, Class<T> objectClass) {
    try {
      InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath);
      return objectMapper.readValue(inputStream, objectMapper.getTypeFactory().constructCollectionType(List.class, objectClass));
    } catch (IOException ex) {
      throw new JsonReadException(ex.getMessage());
    }
  }

  public <T> T readNullableObject(String jsonBody, Class<T> objectClass) {
    try {
      return objectMapper.readValue(jsonBody, objectClass);
    } catch (IOException ex) {
      log.warn("InvalidDeserialization: {}", ex.getMessage());
      return null;
    }
  }

  public <T> Optional<Pair<String, String>> writeCodeAndMessage(String jsonBody, Class<T> objectClass,
                                                                Function<T, Pair<String, String>> mapper) {
    return Optional
        .ofNullable(readNullableObject(jsonBody, objectClass))
        .map(mapper)
        .filter(codeAndMessage -> isNoneEmpty(codeAndMessage.getKey(), codeAndMessage.getValue()));
  }
}