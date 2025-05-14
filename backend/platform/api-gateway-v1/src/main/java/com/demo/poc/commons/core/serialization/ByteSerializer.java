package com.demo.poc.commons.core.serialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

@Slf4j
@RequiredArgsConstructor
public class ByteSerializer {

  private final ObjectMapper objectMapper;

  public <T> byte[] toBytes(T object, String defaultMessage) {
    try {
      return objectMapper.writeValueAsBytes(object);
    } catch (JsonProcessingException ex) {
      log.warn("ByteSerializationError: {}", ex.getMessage());
      return defaultMessage.getBytes(StandardCharsets.UTF_8);
    }
  }
}
