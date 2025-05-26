package com.demo.poc.commons.core.restclient;

import com.demo.poc.commons.core.errors.exceptions.InvalidStreamingData;
import com.demo.poc.commons.core.serialization.JacksonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import reactor.core.publisher.Mono;

import java.util.function.Function;

public interface StreamingTransformer {

  ObjectMapper mapper = JacksonFactory.create();

  static <E> Function<String, Mono<E>> of(Class<E> targetClass) {
    return line -> {
      try {
        return Mono.just(JacksonFactory.create().readValue(line, targetClass));
      } catch (JsonProcessingException exception) {
        throw new InvalidStreamingData(exception.getMessage());
      }
    };
  }

}
