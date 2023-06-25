package com.demo.bbq.business.diningroomorder.infrastructure.repository.restclient.menuoption.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties("application.http-client.menu-option")
public class MenuOptionSelectorClassProperties {

  private Class<?> selectorClass;

  private Class<?> selectorClassFallback;
}
