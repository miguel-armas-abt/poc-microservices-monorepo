package com.demo.bbq.business.waiterorder.infrastructure.documentation.config;

import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DocumentationConfig {

  @Bean
  public GroupedOpenApi publicApi() {
    return GroupedOpenApi.builder()
        .group("BBQ")
        .addOpenApiCustomiser(openApi -> {
          openApi.setInfo(new Info().title("experience-waiter-order-v1").description("This API allows you to attend  to customers"));
        })
        .build();
  }
}
