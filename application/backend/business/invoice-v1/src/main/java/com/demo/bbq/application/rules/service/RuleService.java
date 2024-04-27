package com.demo.bbq.application.rules.service;

public interface RuleService {

  <T> T process(T ruleFilter);

}
