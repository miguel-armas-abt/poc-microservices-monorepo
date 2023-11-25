package com.demo.bbq.business.invoice.infrastructure.rules.service;

public interface RuleService {

  <T> T process(T ruleFilter);

}
