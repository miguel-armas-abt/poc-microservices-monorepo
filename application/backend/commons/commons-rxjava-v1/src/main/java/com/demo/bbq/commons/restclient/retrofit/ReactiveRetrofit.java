package com.demo.bbq.commons.restclient.retrofit;

import com.demo.bbq.commons.errors.exceptions.SystemException;
import com.demo.bbq.commons.toolkit.serialization.JacksonFactory;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.retrofit.CircuitBreakerCallAdapter;
import java.time.Duration;
import java.util.Optional;
import java.util.function.Predicate;
import okhttp3.OkHttpClient;
import org.springframework.http.HttpStatus;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ReactiveRetrofit {

  private String baseUrl;
  private Duration connectTimeout;
  private Duration readTimeout;
  private Duration writeTimeout;
  private CircuitBreaker circuitBreaker;
  private Predicate<Response> responsePredicate;
  private Converter.Factory converterFactory;
  private OkHttpClient.Builder clientBuilder;

  public static boolean isSuccessfulResponse(Response response) {
    return response.code() < HttpStatus.INTERNAL_SERVER_ERROR.value();
  }

  public ReactiveRetrofit connectTimeout(Duration connectTimeout) {
    this.connectTimeout = connectTimeout;
    return this;
  }

  public ReactiveRetrofit readTimeout(Duration readTimeout) {
    this.readTimeout = readTimeout;
    return this;
  }

  public ReactiveRetrofit writeTimeout(Duration writeTimeout) {
    this.writeTimeout = writeTimeout;
    return this;
  }

  public ReactiveRetrofit baseUrl(String baseUrl) {
    this.baseUrl = baseUrl;
    return this;
  }

  public ReactiveRetrofit addConverterFactory(Converter.Factory converterFactory) {
    this.converterFactory = converterFactory;
    return this;
  }

  public ReactiveRetrofit client(OkHttpClient.Builder clientBuilder) {
    this.clientBuilder = clientBuilder;
    return this;
  }

  public static ReactiveRetrofit builder() {
    return new ReactiveRetrofit();
  }

  public <T> T build(Class<T> RepositoryClass) {
    Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
        .client(createOkHttpClient())
        .baseUrl(Optional.ofNullable(baseUrl).orElseThrow(() -> new SystemException("Base URL is required")));

    Optional.ofNullable(circuitBreaker).ifPresent(circuitBreaker ->
        retrofitBuilder.addCallAdapterFactory(
            CircuitBreakerCallAdapter.of(circuitBreaker, Optional.ofNullable(responsePredicate).orElse(ReactiveRetrofit::isSuccessfulResponse))
        )
    );

    retrofitBuilder
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .addConverterFactory(Optional.ofNullable(converterFactory).orElseGet(ScalarsConverterFactory::create))
        .addConverterFactory(Optional.ofNullable(converterFactory).orElseGet(() -> JacksonConverterFactory.create(JacksonFactory.create())))
        .validateEagerly(true);

    return retrofitBuilder.build().create(RepositoryClass);
  }

  private OkHttpClient createOkHttpClient() {
    OkHttpClient.Builder builder = Optional.ofNullable(clientBuilder).orElseGet(OkHttpClient.Builder::new);
    Optional.ofNullable(connectTimeout).ifPresent(builder::connectTimeout);
    Optional.ofNullable(readTimeout).ifPresent(builder::readTimeout);
    Optional.ofNullable(writeTimeout).ifPresent(builder::writeTimeout);
    return builder.build();
  }
}