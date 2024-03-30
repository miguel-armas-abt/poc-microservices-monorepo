package com.demo.bbq.business.orderhub.domain.repository.menu.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties("application.http-client.menu")
public class MenuProperties {

  private Class<?> selectorClass;
}
