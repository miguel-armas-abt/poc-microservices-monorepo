package com.demo.poc.entrypoint.rules.base.service;

public interface RuleService {

  Object processRule(String jsonRequest, String strategy);
}
