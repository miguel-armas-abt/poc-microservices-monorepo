package com.demo.bbq.config.interceptor.logging;

import com.demo.bbq.utils.interceptor.logging.ExchangeResponseFilterUtil;
import com.demo.bbq.utils.properties.ConfigurationBaseProperties;
import com.demo.bbq.utils.restclient.webclient.obfuscation.header.strategy.HeaderObfuscationStrategy;
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
public class ExchangeResponseFilter implements ExchangeFilterFunction {

  private final ConfigurationBaseProperties properties;
  private final List<HeaderObfuscationStrategy> headerObfuscationStrategies;

  @Override
  public Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction next) {
    var context = ExchangeResponseFilterUtil.captureRequest(request);
    return next.exchange(request)
        .contextWrite(context)
        .flatMap(response -> ExchangeResponseFilterUtil.decorateResponse(properties, headerObfuscationStrategies, request, response, context));
  }

}
