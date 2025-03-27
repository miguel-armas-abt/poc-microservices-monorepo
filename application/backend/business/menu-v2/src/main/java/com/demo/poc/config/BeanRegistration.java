package com.demo.poc.config;

import com.demo.poc.commons.tracing.logging.RestClientRequestLogger;
import com.demo.poc.commons.tracing.logging.RestClientResponseLogger;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection(targets = {
    RestClientRequestLogger.class,
    RestClientResponseLogger.class})
public class BeanRegistration {
}
