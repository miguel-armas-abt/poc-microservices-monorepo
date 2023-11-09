package com.demo.bbq.business.waiterorder.infrastructure.repository.restclient.properties;

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

  @Value("${application.http-client.menu-options.v1.base-url}")
  private String menuOptionV1BaseUrl;

  @Value("${application.http-client.menu-options.v2.base-url}")
  private String menuOptionV2BaseUrl;

  @Value("${application.http-client.dining-room-orders.base-url}")
  private String diningRoomBaseUrl;

  @Value("${application.http-client.invoices.base-url}")
  private String invoiceBaseUrl;
}
