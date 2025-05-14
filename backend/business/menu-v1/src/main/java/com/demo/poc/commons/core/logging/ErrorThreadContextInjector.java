package com.demo.poc.commons.core.logging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

@Slf4j
@RequiredArgsConstructor
public class ErrorThreadContextInjector {

  private final ThreadContextInjector injector;

  public void populateFromException(Throwable ex) {
    MDC.clear();
    log.error(ex.getMessage(), ex);
  }
}
