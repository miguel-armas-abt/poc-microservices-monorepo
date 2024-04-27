package com.demo.bbq.application.rules.properties;

import com.demo.bbq.application.properties.ServiceConfigurationProperties;
import lombok.Getter;
import java.util.Map;
import java.util.stream.Collectors;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Getter
@RequiredArgsConstructor
public class RuleInitMap {

  private final ServiceConfigurationProperties properties;
  private Map<String, RuleInfo> ruleInfoMap;

  @PostConstruct
  private void init() {
    ruleInfoMap = properties.getRules().entrySet().stream()
        .collect(Collectors.toMap(o -> o.getValue().getClassName(), Map.Entry::getValue));
  }
}