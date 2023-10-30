package com.demo.bbq.infrastructure.authadapter.infrastructure.repository.restclient.config;

import java.util.concurrent.TimeUnit;

import com.demo.bbq.infrastructure.authadapter.infrastructure.repository.restclient.authenticationprovider.connector.LogoutApi;
import com.demo.bbq.infrastructure.authadapter.infrastructure.repository.restclient.authenticationprovider.connector.RefreshApi;
import com.demo.bbq.infrastructure.authadapter.infrastructure.repository.restclient.authenticationprovider.connector.TokenApi;
import com.demo.bbq.infrastructure.authadapter.infrastructure.repository.restclient.authenticationprovider.connector.UserInfoApi;
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

  @Value("${keycloak.base-uri}")
  private String keycloakUri;


  @Bean
  TokenApi tokenApi(OkHttpClient.Builder builder) {
    return new Retrofit.Builder()
        .baseUrl(keycloakUri)
        .client(client().build())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(JacksonConverterFactory.create())
        .build()
        .create(TokenApi.class);
  }

  @Bean
  RefreshApi refreshApi(OkHttpClient.Builder builder) {
    return new Retrofit.Builder()
        .baseUrl(keycloakUri)
        .client(client().build())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(JacksonConverterFactory.create())
        .build()
        .create(RefreshApi.class);
  }

  @Bean
  LogoutApi logoutApi(OkHttpClient.Builder builder) {
    return new Retrofit.Builder()
        .baseUrl(keycloakUri)
        .client(client().build())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(JacksonConverterFactory.create())
        .build()
        .create(LogoutApi.class);
  }

  @Bean
  UserInfoApi userInfoApi(OkHttpClient.Builder builder) {
    return new Retrofit.Builder()
        .baseUrl(keycloakUri)
        .client(client().build())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(JacksonConverterFactory.create())
        .build()
        .create(UserInfoApi.class);
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
