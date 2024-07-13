package com.demo.bbq.commons.toolkit.rules.service;

public interface RuleService {

  <T> T process(T ruleFilter);

}
