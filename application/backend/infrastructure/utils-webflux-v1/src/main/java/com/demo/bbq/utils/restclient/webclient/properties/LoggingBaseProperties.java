package com.demo.bbq.utils.restclient.webclient.properties;

import com.demo.bbq.utils.restclient.webclient.obfuscation.header.enums.HeaderObfuscationType;
import java.util.Map;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class LoggingBaseProperties {

    protected String projectName;

    protected String projectType;

    protected boolean enabled;

    protected Set<String> sensitiveHeaders;

    protected Map<String, HeaderObfuscationType> headerObfuscationStrategies;

    protected Set<String> sensitiveBodyFields;
}
