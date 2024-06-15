package com.demo.bbq.commons.tracing.logging;

import com.demo.bbq.commons.properties.ConfigurationBaseProperties;
import com.demo.bbq.commons.tracing.logging.constants.LoggingExclusion;
import com.demo.bbq.commons.tracing.logging.constants.LoggingMessage;
import com.demo.bbq.commons.tracing.logging.injector.ThreadContextInjectorUtil;
import com.demo.bbq.commons.tracing.logging.obfuscation.header.HeaderObfuscatorUtil;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    String url = exchange.getRequest().getURI().toString();

    if (LoggingExclusion.isExcluded(url)) {
      return chain.filter(exchange);
    }

    String method = exchange.getRequest().getMethod().toString();
    Map<String, String> requestHeaders = exchange.getRequest().getHeaders().toSingleValueMap();
    String obfuscatedRequestHeaders = HeaderObfuscatorUtil.process(properties.getObfuscation(), requestHeaders);

    ThreadContextInjectorUtil.populateFromHeaders(requestHeaders);
    ThreadContextInjectorUtil.populateFromRestServerRequest(method, url, obfuscatedRequestHeaders, "ToDo");
    log.info(LoggingMessage.REST_SERVER_REQUEST);

    return chain.filter(exchange)
        .doOnSuccess(aVoid -> {
          Map<String, String> responseHeaders = exchange.getResponse().getHeaders().toSingleValueMap();
          String obfuscatedResponseHeaders = HeaderObfuscatorUtil.process(properties.getObfuscation(), requestHeaders);
          String httpCode = exchange.getResponse().getStatusCode().toString();

          ThreadContextInjectorUtil.populateFromHeaders(responseHeaders);
          ThreadContextInjectorUtil.populateFromRestServerResponse(method, url, obfuscatedResponseHeaders, "ToDo", httpCode);
          log.info(LoggingMessage.REST_SERVER_RESPONSE);
        });
    }
}
