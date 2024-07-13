package com.demo.bbq.commons.tracing.logging.obfuscation.header;

import static com.demo.bbq.commons.tracing.logging.obfuscation.constants.ObfuscationConstant.OBFUSCATION_MASK;
import static com.demo.bbq.commons.tracing.logging.obfuscation.constants.ObfuscationConstant.OBFUSCATION_WARNING;

import com.demo.bbq.commons.properties.dto.obfuscation.ObfuscationTemplate;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class HeaderObfuscatorUtil {

    public static String process(ObfuscationTemplate obfuscation,
                                 Map<String, String> headers) {

        return headers
            .entrySet()
            .stream()
            .map(entry -> obfuscateHeader(entry, obfuscation.getHeaders()))
            .collect(Collectors.joining(", "));
    }

    private static String obfuscateHeader(Map.Entry<String, String> header,
                                          Set<String> sensitiveHeaders) {
        String key = header.getKey();
        return !sensitiveHeaders.contains(key)
            ? key + "=" + header.getValue()
            : Optional.ofNullable(header.getValue())
                .map(value -> key + "=" + partiallyObfuscate(value))
                .orElse(key + "=" + OBFUSCATION_WARNING);
    }

    private static String partiallyObfuscate(String value) {
        return value.length() <= 6
            ? OBFUSCATION_MASK
            : value.substring(0, 3) + OBFUSCATION_MASK + value.substring(value.length() - 3);
    }
}