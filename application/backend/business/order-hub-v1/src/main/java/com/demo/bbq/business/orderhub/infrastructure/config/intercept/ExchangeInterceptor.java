package com.demo.bbq.business.orderhub.infrastructure.config.intercept;

import com.demo.bbq.business.orderhub.application.properties.ServiceConfigurationProperties;
import com.demo.bbq.utils.errors.external.RestClientErrorService;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import okhttp3.Interceptor;
import okhttp3.Response;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExchangeInterceptor implements Interceptor {

  private final List<RestClientErrorService> services;
  private final ServiceConfigurationProperties properties;

  @Override
  public Response intercept(Chain chain) throws IOException {
    return chain.proceed(chain.request());
  }

}
