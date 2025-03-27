package com.demo.poc.entrypoint.rules.base.mapper;

import com.demo.poc.entrypoint.rules.base.rule.Rule;

public interface RuleMapper {

  Rule toRule(String jsonRequest);

  Object toResponse(Rule rule);

  boolean supports(String strategy);
}