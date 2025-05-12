package com.demo.poc.commons.core.restclient.utils;

import com.demo.poc.commons.core.properties.restclient.HeaderTemplate;
import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.MultivaluedMap;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static com.demo.poc.commons.core.restclient.utils.ParameterMapFiller.addForwardedParams;
import static com.demo.poc.commons.core.restclient.utils.ParameterMapFiller.addGeneratedParams;
import static com.demo.poc.commons.core.restclient.utils.ParameterMapFiller.addProvidedParams;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HeadersFiller {

  public static MultivaluedMap<String, String> buildHeaders(HeaderTemplate headerTemplate,
                                                            MultivaluedMap<String, String> currentHeaders) {

    Consumer<Map<String, String>> providedHeaders = addProvidedParams(headerTemplate.provided());
    Consumer<Map<String, String>> generatedHeaders = addGeneratedParams(headerTemplate.generated());
    Consumer<Map<String, String>> forwardedHeaders = addForwardedParams(headerTemplate.forwarded(), currentHeaders);

      Map<String, String> headers = new HashMap<>();
      providedHeaders.accept(headers);
      generatedHeaders.accept(headers);
      forwardedHeaders.accept(headers);

      MultivaluedMap<String, String> headersMultivaluedMap = new MultivaluedHashMap<>();
      headers.forEach(headersMultivaluedMap::add);
      return headersMultivaluedMap;
  }
}