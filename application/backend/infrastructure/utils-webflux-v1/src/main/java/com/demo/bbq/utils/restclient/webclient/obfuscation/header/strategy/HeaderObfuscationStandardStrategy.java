package com.demo.bbq.utils.restclient.webclient.obfuscation.header.strategy;

import static com.demo.bbq.utils.restclient.webclient.obfuscation.constants.ObfuscationConstant.OBFUSCATION_MASK;
import com.demo.bbq.utils.properties.dto.HeaderObfuscationType;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class HeaderObfuscationStandardStrategy implements HeaderObfuscationStrategy {

    @Override
    public String obfuscate(String value) {
        if (value.length() <= 6) {
            return OBFUSCATION_MASK;
        } else {
            return value.substring(0, 3) + OBFUSCATION_MASK + value.substring(value.length() - 3);
        }
    }

    @Override
    public boolean supports(HeaderObfuscationType strategy) {
        return HeaderObfuscationType.STANDARD.equals(strategy);
    }
}
