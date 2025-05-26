package com.demo.poc.commons.core.config;

import com.demo.poc.commons.core.errors.dto.ErrorDto;
import com.demo.poc.commons.core.errors.dto.ErrorOrigin;
import com.demo.poc.commons.core.validations.headers.DefaultHeaders;
import io.quarkus.runtime.annotations.RegisterForReflection;

//POJOs not referenced by JPA entities or JAX-RS endpoints
@RegisterForReflection(targets = {
    DefaultHeaders.class,
    ErrorDto.class,
    ErrorOrigin.class,
})
public class ReflectionConfig {
}
