package com.demo.bbq.commons.toolkit.rules.properties;

import com.demo.bbq.commons.properties.ApplicationProperties;
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

  private final ApplicationProperties properties;
  private Map<String, RuleInfo> ruleInfoMap;

  @PostConstruct
  private void init() {
    ruleInfoMap = properties.getRules().entrySet().stream()
        .collect(Collectors.toMap(o -> o.getValue().getClassName(), Map.Entry::getValue));
  }
}