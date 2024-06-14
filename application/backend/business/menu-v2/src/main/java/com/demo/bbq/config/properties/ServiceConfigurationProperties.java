package com.demo.bbq.config.properties;

import com.demo.bbq.commons.properties.ConfigurationBaseProperties;
import io.quarkus.runtime.annotations.StaticInitSafe;
import io.smallrye.config.ConfigMapping;

@StaticInitSafe
@ConfigMapping(prefix = "configuration")
public interface ServiceConfigurationProperties extends ConfigurationBaseProperties {

}