package com.demo.poc.commons.properties;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class ApplicationProperties {

  @Value("${server.mock-port}")
  private int mockPort;
}
