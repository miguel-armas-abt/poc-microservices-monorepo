package com.demo.poc.commons.core.errors.handler.external.strategy;

import org.apache.commons.lang3.tuple.Pair;

import java.util.Optional;

public interface RestClientErrorStrategy extends ExternalErrorStrategy {

  Optional<Pair<String, String>> getCodeAndMessage(String jsonBody);

  boolean supports(Class<? extends ExternalErrorWrapper> wrapperClass);
}