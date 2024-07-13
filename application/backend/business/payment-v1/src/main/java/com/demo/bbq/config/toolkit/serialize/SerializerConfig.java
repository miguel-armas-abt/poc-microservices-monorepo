package com.demo.bbq.config.toolkit.serialize;

import com.demo.bbq.commons.toolkit.serialize.ByteSerializer;
import com.demo.bbq.commons.toolkit.serialize.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SerializerConfig {

  @Bean
  public JsonSerializer jsonSerializer(ObjectMapper objectMapper) {
    return new JsonSerializer(objectMapper);
  }

  @Bean
  public ByteSerializer byteSerializer(ObjectMapper objectMapper) {
    return new ByteSerializer(objectMapper);
  }
}
