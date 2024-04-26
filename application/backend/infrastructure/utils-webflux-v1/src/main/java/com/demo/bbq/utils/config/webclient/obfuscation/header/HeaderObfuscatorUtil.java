package com.demo.bbq.utils.config.webclient.obfuscation.header;

import com.demo.bbq.utils.config.webclient.obfuscation.header.enums.HeaderObfuscationType;
import com.demo.bbq.utils.config.webclient.obfuscation.header.strategy.HeaderObfuscationStrategy;
import com.demo.bbq.utils.config.webclient.properties.LoggingBaseProperties;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.http.HttpHeaders;

public class HeaderObfuscatorUtil {

    public static String process(LoggingBaseProperties loggingProperties,
                          List<HeaderObfuscationStrategy> strategies,
                          HttpHeaders headers) {

        Set<String> sensitiveHeaders = loggingProperties.getSensitiveHeaders();
        Map<String, HeaderObfuscationType> strategiesMap = loggingProperties.getHeaderObfuscationStrategies();
        return headers.toSingleValueMap().entrySet().stream()
                .map(entry -> obfuscateHeader(strategies, entry, sensitiveHeaders, strategiesMap))
                .collect(Collectors.joining(", "));
    }

    private static String obfuscateHeader(List<HeaderObfuscationStrategy> headerObfuscationStrategies,
                                          Map.Entry<String, String> header,
                                          Set<String> sensitiveHeaders,
                                          Map<String, HeaderObfuscationType> strategies) {
        String key = header.getKey();
        if (sensitiveHeaders.contains(key)) {
            HeaderObfuscationType strategyType = strategies.getOrDefault(key, HeaderObfuscationType.STANDARD);

            return headerObfuscationStrategies
                    .stream()
                    .filter(strategy -> strategy.supports(strategyType))
                    .findFirst()
                    .map(strategy -> key + "=" + strategy.obfuscate(header.getValue()))
                    .orElse(key + "=<obfuscation_failed>");
        }
        return key + "=" + header.getValue();
    }
}
