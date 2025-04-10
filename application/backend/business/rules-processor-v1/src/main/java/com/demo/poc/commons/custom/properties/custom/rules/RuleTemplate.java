package com.demo.poc.commons.custom.properties.custom.rules;

import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RuleTemplate {

  private String directory;
  private Map<String, String> strategies;
}