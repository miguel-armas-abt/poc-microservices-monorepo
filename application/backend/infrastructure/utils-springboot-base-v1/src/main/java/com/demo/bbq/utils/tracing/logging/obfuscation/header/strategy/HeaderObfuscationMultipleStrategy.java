package com.demo.bbq.utils.tracing.logging.obfuscation.header.strategy;

import com.demo.bbq.utils.properties.dto.HeaderObfuscationType;
import java.util.Arrays;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class HeaderObfuscationMultipleStrategy implements HeaderObfuscationStrategy {

    private final HeaderObfuscationStrategy standardStrategy;

    @Override
    public String obfuscate(String value) {
        return Arrays.stream(value.split("; "))
                .map(this::obfuscateMultiValue)
                .collect(Collectors.joining("; "));
    }

    private String obfuscateMultiValue(String multiValue) {
        int equalIndex = multiValue.indexOf('=');
        if (equalIndex == -1) {
            return standardStrategy.obfuscate(multiValue);
        }

        String name = multiValue.substring(0, equalIndex + 1);
        String value = multiValue.substring(equalIndex + 1);

        return name + standardStrategy.obfuscate(value);
    }

    @Override
    public boolean supports(HeaderObfuscationType strategy) {
        return HeaderObfuscationType.MULTI.equals(strategy);
    }
}
