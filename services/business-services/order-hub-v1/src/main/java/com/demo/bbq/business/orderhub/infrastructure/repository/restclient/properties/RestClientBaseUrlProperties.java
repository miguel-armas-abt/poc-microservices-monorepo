package com.demo.bbq.business.orderhub.infrastructure.repository.restclient.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@Component
@Getter
@Setter
public class RestClientBaseUrlProperties {

  @Value("${application.http-client.menu.v1.base-url}")
  private String menuV1BaseUrl;

  @Value("${application.http-client.menu.v2.base-url}")
  private String menuV2BaseUrl;

  @Value("${application.http-client.table-placement.base-url}")
  private String tableOrderBaseUrl;

  @Value("${application.http-client.invoice.base-url}")
  private String invoiceBaseUrl;
}
