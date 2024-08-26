package com.demo.bbq.commons.toolkit.params.filler;

import static com.demo.bbq.commons.toolkit.params.filler.ParameterMapFiller.*;

import com.demo.bbq.commons.properties.dto.restclient.HeaderTemplate;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HeadersFiller {

  public static Map<String, String> fillHeaders(HeaderTemplate headerTemplate,
                                                Map<String, String> currentHeaders) {

    Consumer<Map<String, String>> providedHeaders = addProvidedParams(headerTemplate.getProvided());
    Consumer<Map<String, String>> generatedHeaders = addGeneratedParams(headerTemplate.getGenerated());
    Consumer<Map<String, String>> forwardedHeaders = addForwardedParams(headerTemplate.getForwarded(), currentHeaders);

    Map<String, String> headers = new HashMap<>();
    providedHeaders.accept(headers);
    generatedHeaders.accept(headers);
    forwardedHeaders.accept(headers);

    return headers;
  }
}