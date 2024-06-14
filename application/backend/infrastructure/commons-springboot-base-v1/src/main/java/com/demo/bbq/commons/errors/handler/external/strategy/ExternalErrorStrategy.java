package com.demo.bbq.commons.errors.handler.external.strategy;

public interface ExternalErrorStrategy {

  boolean supports(Class<? extends ExternalErrorWrapper> wrapperClass);
}
