package com.demo.bbq.utils.config.webclient.obfuscation.header.strategy;

import static com.demo.bbq.utils.config.webclient.obfuscation.constants.ObfuscationConstant.OBFUSCATION_MASK;
import static com.demo.bbq.utils.config.webclient.obfuscation.header.enums.HeaderObfuscationType.STANDARD;

import com.demo.bbq.utils.config.webclient.obfuscation.header.enums.HeaderObfuscationType;
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
        return STANDARD.equals(strategy);
    }
}
