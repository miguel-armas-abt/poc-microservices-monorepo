package com.demo.bbq.config.interceptor;

import com.demo.bbq.utils.restclient.webclient.properties.LoggingBaseProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "custom-logging")
public class LoggingProperties extends LoggingBaseProperties {
}
