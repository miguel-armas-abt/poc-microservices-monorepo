package com.demo.bbq.business.orderhub.domain.repository.invoice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@Component
@Getter
@Setter
public class InvoiceRestClientProperties {

  @Value("${application.http-client.invoice.base-url}")
  private String baseURL;
}
