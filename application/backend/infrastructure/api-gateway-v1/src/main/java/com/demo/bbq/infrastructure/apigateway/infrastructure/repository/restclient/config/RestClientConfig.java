package com.demo.bbq.infrastructure.apigateway.infrastructure.repository.restclient.config;

import com.demo.bbq.infrastructure.apigateway.infrastructure.repository.restclient.authadapter.AuthAdapterApi;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;
@Configuration
public class RestClientConfig {

  @Value("${application.http-client.auth-adapter.base-url}")
  private String authAdapterBaseUrl;

  @Bean
  AuthAdapterApi authAdapterApi(OkHttpClient.Builder builder) {
    return new Retrofit.Builder()
        .baseUrl(authAdapterBaseUrl)
        .client(client().build())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(JacksonConverterFactory.create())
        .build()
        .create(AuthAdapterApi.class);
  }

  @Bean
  OkHttpClient.Builder client() {
    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
    interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
    return new OkHttpClient.Builder()
        .readTimeout(15, TimeUnit.SECONDS)
        .connectTimeout(15, TimeUnit.SECONDS)
        .addInterceptor(interceptor)
        .addInterceptor(new ApiExceptionInterceptor());
  }
}
