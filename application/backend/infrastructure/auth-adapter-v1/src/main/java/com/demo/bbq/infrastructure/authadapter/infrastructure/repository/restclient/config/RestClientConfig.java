package com.demo.bbq.infrastructure.authadapter.infrastructure.repository.restclient.config;

import com.demo.bbq.infrastructure.authadapter.infrastructure.repository.restclient.authenticationprovider.connector.AuthenticationProviderApi;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Configuration
public class RestClientConfig {

  @Value("${keycloak.base-uri}")
  private String keycloakUri;


  @Bean
  AuthenticationProviderApi tokenApi(OkHttpClient.Builder builder) {
    return new Retrofit.Builder()
        .baseUrl(keycloakUri)
        .client(client().build())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .addConverterFactory(JacksonConverterFactory.create())
        .build()
        .create(AuthenticationProviderApi.class);
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
