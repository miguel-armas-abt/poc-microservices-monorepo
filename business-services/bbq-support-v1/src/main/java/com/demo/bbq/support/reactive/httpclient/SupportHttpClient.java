package com.demo.bbq.support.reactive.httpclient;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.retrofit.CircuitBreakerCallAdapter;
import java.text.DateFormat;
import java.time.Duration;
import java.util.Optional;
import java.util.function.Predicate;
import okhttp3.OkHttpClient;
import org.springframework.http.HttpStatus;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

@SuppressFBWarnings(value = {"UWF_FIELD_NOT_INITIALIZED_IN_CONSTRUCTOR"}, justification = "This class is using build pattern")
public class SupportHttpClient {
  private String baseUrl;
  private Duration connectTimeout;
  private Duration readTimeout;
  private Duration writeTimeout;
  private CircuitBreaker circuitBreaker;
  private Predicate<Response> responsePredicate;
  private Converter.Factory converterFactory;
  private OkHttpClient.Builder clientBuilder;

  public static boolean successResponsePredicate(Response response) {
    int code = response.code();
    return (code < HttpStatus.INTERNAL_SERVER_ERROR.value());
  }

  public SupportHttpClient connectTimeout(Duration connectTimeout) {
    this.connectTimeout = connectTimeout;
    return this;
  }

  public SupportHttpClient readTimeout(Duration readTimeout) {
    this.readTimeout = readTimeout;
    return this;
  }

  public SupportHttpClient writeTimeout(Duration writeTimeout) {
    this.writeTimeout = writeTimeout;
    return this;
  }

  public SupportHttpClient baseUrl(String baseUrl) {
    this.baseUrl = baseUrl;
    return this;
  }

  public SupportHttpClient converterFactory(Converter.Factory converterFactory) {
    this.converterFactory = converterFactory;
    return this;
  }

  public SupportHttpClient clientBuilder(OkHttpClient.Builder clientBuilder) {
    this.clientBuilder = clientBuilder;
    return this;
  }

  public static SupportHttpClient builder() {
    return new SupportHttpClient();
  }

  public <T> T buildProxy(Class<T> proxyClass) {
    this.clientBuilder = Optional.<OkHttpClient.Builder>ofNullable(this.clientBuilder).orElseGet(okhttp3.OkHttpClient.Builder::new);
    Optional.<Duration>ofNullable(this.connectTimeout)
        .ifPresent(timeout -> this.clientBuilder.connectTimeout(timeout));
    Optional.<Duration>ofNullable(this.readTimeout)
        .ifPresent(timeout -> this.clientBuilder.readTimeout(timeout));
    Optional.<Duration>ofNullable(this.writeTimeout)
        .ifPresent(timeout -> this.clientBuilder.writeTimeout(timeout));
    Retrofit.Builder builder = (new Retrofit.Builder()).client(this.clientBuilder.build());
    if (this.circuitBreaker != null)
      builder.addCallAdapterFactory((this.responsePredicate != null) ?
          (CallAdapter.Factory)CircuitBreakerCallAdapter.of(this.circuitBreaker, this.responsePredicate) :
          (CallAdapter.Factory)CircuitBreakerCallAdapter.of(this.circuitBreaker));
    return (T)builder.addCallAdapterFactory((CallAdapter.Factory)RxJava2CallAdapterFactory.create())
        .addConverterFactory(Optional.<Converter.Factory>ofNullable(this.converterFactory)
            .orElseGet(ScalarsConverterFactory::create))
        .addConverterFactory(Optional.<Converter.Factory>ofNullable(this.converterFactory)
            .orElseGet(() -> JacksonConverterFactory.create(getObjectMapper())))
        .validateEagerly(true).baseUrl(this.baseUrl).build().create(proxyClass);
  }

  private ObjectMapper getObjectMapper() {
    return (new ObjectMapper()).setSerializationInclusion(JsonInclude.Include.NON_NULL)
        .setDateFormat((DateFormat)new StdDateFormat())
        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        .enable(new MapperFeature[] { MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES }).enable(new JsonGenerator.Feature[] { JsonGenerator.Feature.IGNORE_UNKNOWN }).enable(new JsonParser.Feature[] { JsonParser.Feature.ALLOW_COMMENTS });
  }
}
