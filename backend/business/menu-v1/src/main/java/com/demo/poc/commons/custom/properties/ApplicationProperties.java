package com.demo.poc.commons.custom.properties;

import com.demo.poc.commons.core.properties.ConfigurationBaseProperties;
import io.quarkus.runtime.annotations.StaticInitSafe;
import io.smallrye.config.ConfigMapping;

@StaticInitSafe
@ConfigMapping(prefix = "configuration")
public interface ApplicationProperties extends ConfigurationBaseProperties {
}