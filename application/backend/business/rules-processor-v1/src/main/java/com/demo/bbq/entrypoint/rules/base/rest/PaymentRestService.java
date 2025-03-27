package com.demo.bbq.entrypoint.rules.base.rest;

import com.demo.bbq.commons.core.restclient.utils.HttpHeadersFiller;
import com.demo.bbq.commons.core.validations.headers.HeaderValidator;
import com.demo.bbq.entrypoint.rules.base.dto.params.StrategyHeader;
import com.demo.bbq.entrypoint.rules.base.service.RuleService;
import io.reactivex.rxjava3.core.Single;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/bbq/business/rules-processor/v1", produces = MediaType.APPLICATION_JSON_VALUE)
public class PaymentRestService {

  private final RuleService ruleService;
  private final HeaderValidator headerValidator;

  @PostMapping(value = "/execute")
  public Single<Object> execute(HttpServletRequest httpServletRequest,
                                @RequestBody String jsonRequest) {

    return Single.just(headerValidator.validateAndRetrieve(HttpHeadersFiller.extractHeadersAsMap(httpServletRequest), StrategyHeader.class))
        .map(headers -> ruleService.processRule(jsonRequest, headers.getStrategy()));
  }
}