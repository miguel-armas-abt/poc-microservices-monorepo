package com.demo.bbq.utils.tracing.logging.obfuscation.header.strategy;

import com.demo.bbq.utils.properties.dto.HeaderObfuscationType;

public interface HeaderObfuscationStrategy {

    String obfuscate(String value);

    boolean supports(HeaderObfuscationType strategy);

}
