package com.demo.bbq.config.interceptor;

import com.demo.bbq.application.properties.ServiceConfigurationProperties;
import java.io.IOException;
import java.util.List;

import com.demo.bbq.utils.errors.handler.external.strategy.RestClientErrorStrategy;
import lombok.RequiredArgsConstructor;
import okhttp3.Interceptor;
import okhttp3.Response;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExchangeInterceptor implements Interceptor {

  private final List<RestClientErrorStrategy> strategies;
  private final ServiceConfigurationProperties properties;

  @Override
  public Response intercept(Chain chain) throws IOException {
    return chain.proceed(chain.request());
  }

}