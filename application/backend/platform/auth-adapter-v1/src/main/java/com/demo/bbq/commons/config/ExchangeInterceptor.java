package com.demo.bbq.commons.config;

import com.demo.bbq.commons.properties.ApplicationProperties;
import com.demo.bbq.commons.errors.handler.external.strategy.RestClientErrorStrategy;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import okhttp3.Interceptor;
import okhttp3.Response;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExchangeInterceptor implements Interceptor {

  private final List<RestClientErrorStrategy> strategies;
  private final ApplicationProperties properties;

  @Override
  public Response intercept(Chain chain) throws IOException {
    return chain.proceed(chain.request());
  }

}