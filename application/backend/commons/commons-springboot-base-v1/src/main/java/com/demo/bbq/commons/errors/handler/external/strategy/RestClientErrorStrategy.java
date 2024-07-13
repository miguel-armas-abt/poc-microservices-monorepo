package com.demo.bbq.commons.errors.handler.external.strategy;

import java.util.Optional;
import org.apache.commons.lang3.tuple.Pair;

public interface RestClientErrorStrategy extends ExternalErrorStrategy {

  Optional<Pair<String, String>> getCodeAndMessage(String jsonBody);

  boolean supports(Class<? extends ExternalErrorWrapper> wrapperClass);
}