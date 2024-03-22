package com.demo.bbq.business.invoice.application.rules.service;

public interface RuleService {

  <T> T process(T ruleFilter);

}
