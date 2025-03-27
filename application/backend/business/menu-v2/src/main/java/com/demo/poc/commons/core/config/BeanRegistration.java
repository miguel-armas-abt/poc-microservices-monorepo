package com.demo.poc.commons.core.config;

import com.demo.poc.commons.core.interceptor.error.ConstraintErrorInterceptor;
import com.demo.poc.commons.core.interceptor.error.ErrorInterceptor;
import com.demo.poc.commons.core.interceptor.restclient.RestClientRequestInterceptor;
import com.demo.poc.commons.core.interceptor.restclient.RestClientResponseInterceptor;
import com.demo.poc.commons.core.logging.ThreadContextInjector;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection(targets = {
    ThreadContextInjector.class,
    ErrorInterceptor.class,
    ConstraintErrorInterceptor.class,
    RestClientRequestInterceptor.class,
    RestClientResponseInterceptor.class})
public class BeanRegistration {
}
