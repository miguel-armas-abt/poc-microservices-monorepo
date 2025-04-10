package com.demo.poc.commons.core.restclient.error;

import org.apache.commons.lang3.tuple.Pair;

import java.util.Optional;

public interface RestClientErrorExtractor {

  Optional<Pair<String, String>> getCodeAndMessage(String jsonBody);

  boolean supports(Class<?> wrapperClass);
}