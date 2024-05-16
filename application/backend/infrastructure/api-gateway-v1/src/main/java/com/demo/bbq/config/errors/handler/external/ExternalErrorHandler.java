package com.demo.bbq.config.errors.handler.external;

import com.demo.bbq.config.properties.ServiceConfigurationProperties;
import com.demo.bbq.utils.errors.handler.external.ExternalErrorHandlerUtil;
import com.demo.bbq.utils.errors.handler.external.strategy.ExternalErrorWrapper;
import com.demo.bbq.utils.errors.handler.external.strategy.RestClientErrorStrategy;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ExternalErrorHandler {

  private final List<RestClientErrorStrategy> strategies;
  private final ServiceConfigurationProperties properties;

  public Mono<? extends Throwable> handleError(ClientResponse clientResponse,
                                               Class<? extends ExternalErrorWrapper> errorWrapperClass,
                                               String serviceName) {
    return ExternalErrorHandlerUtil.build(clientResponse, errorWrapperClass, serviceName, strategies, properties);
  }
}
