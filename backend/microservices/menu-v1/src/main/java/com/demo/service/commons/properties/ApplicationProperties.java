package com.demo.service.commons.properties;

import com.demo.commons.properties.ConfigurationBaseProperties;
import io.quarkus.runtime.annotations.StaticInitSafe;
import io.smallrye.config.ConfigMapping;

@StaticInitSafe
@ConfigMapping(prefix = "configuration")
public interface ApplicationProperties extends ConfigurationBaseProperties {
}