package com.demo.bbq.config.tracing.logging;

import com.demo.bbq.utils.properties.ConfigurationBaseProperties;
import com.demo.bbq.utils.tracing.logging.ClientResponseLoggingUtil;
import com.demo.bbq.utils.tracing.logging.obfuscation.header.strategy.HeaderObfuscationStrategy;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ClientResponseLogging implements ExchangeFilterFunction {

  private final ConfigurationBaseProperties properties;
  private final List<HeaderObfuscationStrategy> headerObfuscationStrategies;

  @Override
  public Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction next) {
    var context = ClientResponseLoggingUtil.captureRequest(request);
    return next.exchange(request)
        .contextWrite(context)
        .flatMap(response -> ClientResponseLoggingUtil.decorateResponse(properties, headerObfuscationStrategies, request, response, context));
  }
}
