package com.demo.bbq.business.orderhub.domain.repository.tableorder.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@Component
@Getter
@Setter
public class TableOrderRestClientProperties {

  @Value("${application.http-client.table-placement.base-url}")
  private String baseURL;
}
