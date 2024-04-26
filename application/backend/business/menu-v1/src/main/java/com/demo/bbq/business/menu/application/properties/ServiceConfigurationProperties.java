package com.demo.bbq.business.menu.application.properties;

import com.demo.bbq.utils.properties.ConfigurationBaseProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "configuration")
public class ServiceConfigurationProperties extends ConfigurationBaseProperties {

}
