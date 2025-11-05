package com.demo.service.commons.config.reflection;

import io.quarkus.runtime.annotations.RegisterForReflection;

//POJOs not referenced by JPA entities or JAX-RS endpoints
@RegisterForReflection(targets = {

})
public class ReflectionConfig {
}
