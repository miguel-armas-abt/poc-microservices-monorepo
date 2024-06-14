package com.demo.bbq.config;

import com.demo.bbq.commons.tracing.logging.RestClientRequestLogger;
import com.demo.bbq.commons.tracing.logging.RestClientResponseLogger;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection(targets = {
    RestClientRequestLogger.class,
    RestClientResponseLogger.class})
public class BeanRegistration {
}
