package com.demo.bbq.utils.errors.handler.external.strategy;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.web.client.HttpStatusCodeException;

public interface RestClientErrorStrategy extends ExternalErrorStrategy {

  Pair<String, String> getCodeAndMessage(HttpStatusCodeException httpException);

}