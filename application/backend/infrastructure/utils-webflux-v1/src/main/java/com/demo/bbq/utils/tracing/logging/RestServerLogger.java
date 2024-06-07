package com.demo.bbq.utils.tracing.logging;

import com.demo.bbq.utils.properties.ConfigurationBaseProperties;
import com.demo.bbq.utils.tracing.logging.constants.LoggingMessage;
import com.demo.bbq.utils.tracing.logging.injector.ThreadContextInjectorUtil;
import com.demo.bbq.utils.tracing.logging.obfuscation.header.HeaderObfuscatorUtil;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
public class RestServerLogger implements WebFilter {

  private final ConfigurationBaseProperties properties;

  //ToDo: Add requestBody && responseBody in logs
  @Override
  public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
    String method = exchange.getRequest().getMethod().toString();
    String url = exchange.getRequest().getURI().toString();
    Map<String, String> requestHeaders = exchange.getRequest().getHeaders().toSingleValueMap();
    String obfuscatedRequestHeaders = HeaderObfuscatorUtil.process(properties.getObfuscation(), requestHeaders);

    ThreadContextInjectorUtil.populateFromHeaders(requestHeaders);
    ThreadContextInjectorUtil.populateFromRestServerRequest(method, url, obfuscatedRequestHeaders, StringUtils.EMPTY);
    log.info(LoggingMessage.REST_SERVER_REQUEST);

    return chain.filter(exchange)
        .doOnSuccess(aVoid -> {
          Map<String, String> responseHeaders = exchange.getResponse().getHeaders().toSingleValueMap();
          String obfuscatedResponseHeaders = HeaderObfuscatorUtil.process(properties.getObfuscation(), requestHeaders);
          String httpCode = exchange.getResponse().getStatusCode().toString();

          ThreadContextInjectorUtil.populateFromHeaders(responseHeaders);
          ThreadContextInjectorUtil.populateFromRestServerResponse(method, url, obfuscatedResponseHeaders, StringUtils.EMPTY, httpCode);
          log.info(LoggingMessage.REST_SERVER_RESPONSE);
        });
  }
}
