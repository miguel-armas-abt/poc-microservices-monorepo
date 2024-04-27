package com.demo.bbq.utils.restclient.webclient.obfuscation.header.strategy;

import com.demo.bbq.utils.restclient.webclient.obfuscation.header.enums.HeaderObfuscationType;

public interface HeaderObfuscationStrategy {

    String obfuscate(String value);

    boolean supports(HeaderObfuscationType strategy);

}
