package com.demo.bbq.business.invoice.application.properties;

import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

@RefreshScope
@ConfigurationProperties
@Configuration
public class InvoiceProperties {

  @Value("${business.invoice.igv}")
  private String igv;

  public BigDecimal getIgv() {
    return new BigDecimal(igv);
  }
}
