package com.demo.bbq.business.orderhub.infrastructure.rules.service;

public interface RuleService {

  <T> T process(T ruleFilter);

}
