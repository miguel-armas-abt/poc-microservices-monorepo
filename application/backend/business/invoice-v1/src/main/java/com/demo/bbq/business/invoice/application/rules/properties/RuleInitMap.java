package com.demo.bbq.business.invoice.application.rules.properties;

import lombok.Getter;
import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Getter
@RequiredArgsConstructor
public class RuleInitMap {

  private final RuleProperties ruleProperties;
  private Map<String, RuleInfo> ruleInfoMap;

  @PostConstruct
  private void init() {
    ruleInfoMap = ruleProperties.getRules().entrySet().stream()
        .collect(Collectors.toMap(o -> o.getValue().getRuleClass(), Map.Entry::getValue));
  }
}