package com.demo.bbq.infrastructure.apigateway.infrastructure.config.logging;

import com.demo.bbq.utils.config.webclient.logging.ExchangeResponseFilterUtil;
import com.demo.bbq.utils.config.webclient.obfuscation.header.strategy.HeaderObfuscationStrategy;
import com.demo.bbq.utils.config.webclient.properties.LoggingBaseProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ExchangeResponseFilter implements ExchangeFilterFunction {

  private final LoggingBaseProperties properties;
  private final List<HeaderObfuscationStrategy> headerObfuscationStrategies;

  @Override
  public Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction next) {
    if (!properties.isEnabled()) {
      return next.exchange(request);
    }

    var context = ExchangeResponseFilterUtil.captureRequestDetails(request);
    return next.exchange(request)
        .contextWrite(context)
        .flatMap(response -> ExchangeResponseFilterUtil.handleResponse(properties, headerObfuscationStrategies, request, response, context));
  }

}
