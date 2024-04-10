package com.demo.bbq.infrastructure.apigateway.application.properties;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

@Getter
@RefreshScope
@ConfigurationProperties
@Configuration
public class RolesProperties {

  @Value("#{${application.user-roles}}")
  private Map<Integer, String> rolesList = new HashMap<>();

}
