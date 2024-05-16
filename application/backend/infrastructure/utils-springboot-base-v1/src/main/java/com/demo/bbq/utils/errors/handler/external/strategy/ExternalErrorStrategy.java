package com.demo.bbq.utils.errors.handler.external.strategy;

public interface ExternalErrorStrategy {

  boolean supports(Class<? extends ExternalErrorWrapper> wrapperClass);
}
