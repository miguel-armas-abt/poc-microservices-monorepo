package com.demo.poc.entrypoint.rules.strategies.discount.mapper;

import com.demo.poc.entrypoint.rules.base.rule.Rule;
import com.demo.poc.entrypoint.rules.base.mapper.RuleMapper;
import com.demo.poc.entrypoint.rules.strategies.discount.dto.DiscountRuleRequestDTO;
import com.demo.poc.entrypoint.rules.strategies.discount.dto.DiscountRuleResponseDTO;
import com.demo.poc.entrypoint.rules.strategies.discount.rule.DiscountRule;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DiscountRuleMapper implements RuleMapper {

  private final Gson gson;

  @Override
  public Rule toRule(String jsonRequest) {
    DiscountRuleRequestDTO discountRuleRequest = gson.fromJson(jsonRequest, DiscountRuleRequestDTO.class);
    return DiscountRule.builder()
        .quantity(discountRuleRequest.getQuantity())
        .productCode(discountRuleRequest.getProductCode())
        .build();
  }

  @Override
  public Object toResponse(Rule rule) {
    DiscountRule discountRule = (DiscountRule) rule;
    return DiscountRuleResponseDTO.builder()
        .discount(discountRule.getDiscount())
        .build();
  }

  @Override
  public boolean supports(String strategy) {
    return DiscountRule.class.getSimpleName().equals(strategy);
  }

}