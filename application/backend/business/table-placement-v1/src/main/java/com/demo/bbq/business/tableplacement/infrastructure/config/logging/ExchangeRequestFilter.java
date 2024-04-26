package com.demo.bbq.business.tableplacement.infrastructure.config.logging;

import com.demo.bbq.utils.config.webclient.logging.ExchangeRequestFilterUtil;
import com.demo.bbq.utils.config.webclient.obfuscation.header.strategy.HeaderObfuscationStrategy;
import com.demo.bbq.utils.config.webclient.properties.LoggingBaseProperties;
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
public class ExchangeRequestFilter implements ExchangeFilterFunction {

  private final LoggingBaseProperties properties;
  private final List<HeaderObfuscationStrategy> headerObfuscationStrategies;

  @Override
  public Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction next) {
    if (!properties.isEnabled()) {
      return next.exchange(request);
    }
    return next.exchange(ExchangeRequestFilterUtil.buildClientRequestDecorator(properties, headerObfuscationStrategies, request));
  }
}
