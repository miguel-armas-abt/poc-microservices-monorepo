package com.demo.bbq.config.properties;

import com.demo.bbq.commons.properties.ConfigurationBaseProperties;
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
