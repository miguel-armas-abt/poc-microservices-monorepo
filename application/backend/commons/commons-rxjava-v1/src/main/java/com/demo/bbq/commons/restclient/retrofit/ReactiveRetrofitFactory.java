package com.demo.bbq.commons.restclient.retrofit;

import com.demo.bbq.commons.errors.exceptions.SystemException;
import com.demo.bbq.commons.properties.ConfigurationBaseProperties;
import com.demo.bbq.commons.restclient.enums.TimeoutLevel;
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

public class ReactiveRetrofitFactory {

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

  public ReactiveRetrofitFactory connectTimeout(Duration connectTimeout) {
    this.connectTimeout = connectTimeout;
    return this;
  }

  public ReactiveRetrofitFactory readTimeout(Duration readTimeout) {
    this.readTimeout = readTimeout;
    return this;
  }

  public ReactiveRetrofitFactory writeTimeout(Duration writeTimeout) {
    this.writeTimeout = writeTimeout;
    return this;
  }

  public ReactiveRetrofitFactory baseUrl(String baseUrl) {
    this.baseUrl = baseUrl;
    return this;
  }

  public ReactiveRetrofitFactory addConverterFactory(Converter.Factory converterFactory) {
    this.converterFactory = converterFactory;
    return this;
  }

  public ReactiveRetrofitFactory client(OkHttpClient.Builder clientBuilder) {
    this.clientBuilder = clientBuilder;
    return this;
  }

  public static ReactiveRetrofitFactory builder() {
    return new ReactiveRetrofitFactory();
  }

  public <T> T build(Class<T> RepositoryClass) {
    Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
        .client(createOkHttpClient())
        .baseUrl(Optional.ofNullable(baseUrl).orElseThrow(() -> new SystemException("Base URL is required")));

    Optional.ofNullable(circuitBreaker).ifPresent(circuitBreaker ->
        retrofitBuilder.addCallAdapterFactory(
            CircuitBreakerCallAdapter.of(circuitBreaker, Optional.ofNullable(responsePredicate).orElse(ReactiveRetrofitFactory::isSuccessfulResponse))
        )
    );

    retrofitBuilder
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .addConverterFactory(Optional.ofNullable(converterFactory).orElseGet(ScalarsConverterFactory::create))
        .addConverterFactory(Optional.ofNullable(converterFactory).orElseGet(() -> JacksonConverterFactory.create(JacksonFactory.create())))
        .validateEagerly(true);

    return retrofitBuilder.build().create(RepositoryClass);
  }

  public static <T> T create(OkHttpClient.Builder builder, ConfigurationBaseProperties properties,
                      String serviceName, Class<T> repositoryClass) {

    TimeoutLevel timeoutLevel = properties.searchPerformance(serviceName).getTimeout();
    return ReactiveRetrofitFactory.builder()
        .client(builder)
        .baseUrl(properties.searchEndpoint(serviceName))
        .connectTimeout(timeoutLevel.getConnectionTimeoutDuration())
        .readTimeout(timeoutLevel.getReadTimeoutDuration())
        .writeTimeout(timeoutLevel.getWriteTimeoutDuration())
        .build(repositoryClass);
  }

  private OkHttpClient createOkHttpClient() {
    OkHttpClient.Builder builder = Optional.ofNullable(clientBuilder).orElseGet(OkHttpClient.Builder::new);
    Optional.ofNullable(connectTimeout).ifPresent(builder::connectTimeout);
    Optional.ofNullable(readTimeout).ifPresent(builder::readTimeout);
    Optional.ofNullable(writeTimeout).ifPresent(builder::writeTimeout);
    return builder.build();
  }
}