package com.demo.bbq.infrastructure.authadapter.domain.repository.authprovider.config;

import com.demo.bbq.infrastructure.authadapter.domain.repository.authprovider.AuthProviderRepository;
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

  private final AuthProviderRestClientProperties properties;

  @Bean
  AuthProviderRepository buildAuthProviderRepository(OkHttpClient.Builder builder) {
    return new Retrofit.Builder()
        .baseUrl(properties.getBaseURL())
        .client(builder.build())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .addConverterFactory(JacksonConverterFactory.create())
        .build()
        .create(AuthProviderRepository.class);
  }

}
