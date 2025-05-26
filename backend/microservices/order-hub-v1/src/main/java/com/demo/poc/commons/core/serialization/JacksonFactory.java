package com.demo.poc.commons.core.serialization;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

public interface JacksonFactory {

  static Jackson2ObjectMapperBuilder builder() {
    return new Jackson2ObjectMapperBuilder()
        .modules(new JavaTimeModule(), new Jdk8Module())
        .serializationInclusion(JsonInclude.Include.NON_NULL)
        .dateFormat(new StdDateFormat())
        .featuresToDisable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        .featuresToEnable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
        .featuresToEnable(JsonGenerator.Feature.IGNORE_UNKNOWN)
        .featuresToEnable(JsonParser.Feature.ALLOW_COMMENTS);
  }

  static ObjectMapper create() {
    return builder().createXmlMapper(false).build();
  }
}
