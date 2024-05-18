package com.demo.bbq.utils.restclient.webclient.obfuscation.header;

import com.demo.bbq.utils.properties.dto.HeaderObfuscationType;
import com.demo.bbq.utils.properties.dto.ObfuscationTemplate;
import com.demo.bbq.utils.restclient.webclient.obfuscation.header.strategy.HeaderObfuscationStrategy;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.http.HttpHeaders;

public class HeaderObfuscatorUtil {

    public static String process(ObfuscationTemplate obfuscation,
                                 List<HeaderObfuscationStrategy> strategies,
                                 HttpHeaders headers) {

        Set<String> sensitiveHeaders = obfuscation.getHeaders();
        Map<String, HeaderObfuscationType> strategiesMap = obfuscation.getHeaderObfuscationType();
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
