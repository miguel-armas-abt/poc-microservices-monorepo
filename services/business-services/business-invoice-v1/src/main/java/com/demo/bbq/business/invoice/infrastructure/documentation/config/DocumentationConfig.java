package com.demo.bbq.business.invoice.infrastructure.documentation.config;

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
          openApi.setInfo(new Info().title("business-invoice-v1").description("This API allows you to consult the proforma invoice and send it to pay"));
        })
        .build();
  }
}
