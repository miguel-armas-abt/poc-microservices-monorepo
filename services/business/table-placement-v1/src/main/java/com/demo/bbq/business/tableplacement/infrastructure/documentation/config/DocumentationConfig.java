package com.demo.bbq.business.tableplacement.infrastructure.documentation.config;

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
          openApi.setInfo(new Info().title("table-placement-v1").description("This API allows you to consult and generate orders of a table on dining room"));
        })
        .build();
  }
}
