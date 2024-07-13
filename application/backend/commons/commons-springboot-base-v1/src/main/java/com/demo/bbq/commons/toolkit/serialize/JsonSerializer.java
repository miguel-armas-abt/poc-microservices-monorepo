package com.demo.bbq.commons.toolkit.serialize;

import com.demo.bbq.commons.errors.exceptions.SystemException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;

@Slf4j
@RequiredArgsConstructor
public class JsonSerializer {

  private final ObjectMapper objectMapper;

  @PostConstruct
  private void configureObjectMapper() {
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
  }

  public <T> T readElementFromFile(String filePath, Class<T> classSource) {
    try {
      return objectMapper.readValue(JsonSerializer.class.getClassLoader().getResourceAsStream(filePath), classSource);
    } catch (IOException ex) {
      throw new SystemException("ErrorGettingElement: {}", ex.getMessage());
    }
  }

  public <T> List<T> readListFromFile(String filePath, Class<T[]> classSource) {
    try {
      T[] array = objectMapper.readValue(Objects.requireNonNull(JsonSerializer.class.getClassLoader().getResourceAsStream(filePath)), classSource);
      return Arrays.asList(array);
    } catch (IOException ex) {
      throw new SystemException("ErrorGettingList: {}", ex.getMessage());
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
        .map(mapper);
  }
}