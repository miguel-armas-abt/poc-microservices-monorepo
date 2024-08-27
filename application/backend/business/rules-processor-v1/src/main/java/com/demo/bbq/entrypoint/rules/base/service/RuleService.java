package com.demo.bbq.entrypoint.rules.base.service;

public interface RuleService {

  Object processRule(String jsonRequest, String strategy);
}
