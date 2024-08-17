package com.demo.bbq.entrypoint.auth.repository.authprovider.config;

import com.demo.bbq.commons.properties.ApplicationProperties;
import com.demo.bbq.entrypoint.auth.repository.authprovider.AuthProviderRepository;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Configuration
@RequiredArgsConstructor
public class AuthProviderRestClientConfig {

  public static final String SERVICE_NAME = "keycloak";
  private final ApplicationProperties properties;

  @Bean(SERVICE_NAME)
  AuthProviderRepository create(OkHttpClient.Builder builder) {
    return new Retrofit.Builder()
        .baseUrl(properties.searchEndpoint(SERVICE_NAME))
        .client(builder.build())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .addConverterFactory(JacksonConverterFactory.create())
        .build()
        .create(AuthProviderRepository.class);
  }

}
