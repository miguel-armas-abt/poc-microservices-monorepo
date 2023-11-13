package com.demo.bbq.business.orderhub.infrastructure.rules.properties;

import lombok.Data;
import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

@RefreshScope
@Configuration
@ConfigurationProperties("business")
@Data
public class RuleProperties {

  private Map<String, RuleInfo> rules;

}
