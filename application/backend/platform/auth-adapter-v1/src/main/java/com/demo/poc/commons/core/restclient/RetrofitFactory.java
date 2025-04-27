package com.demo.poc.commons.core.restclient;

import com.demo.poc.commons.core.errors.exceptions.EmptyBaseUrlException;
import com.demo.poc.commons.core.properties.restclient.RestClient;
import com.demo.poc.commons.core.restclient.enums.TimeoutLevel;
import com.demo.poc.commons.core.serialization.JacksonFactory;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RetrofitFactory {

  public static <T> T create(OkHttpClient.Builder okHttpClient,
                             RestClient restClient,
                             Class<T> RepositoryClass) {

    Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
        .client(createOkHttpClient(okHttpClient, restClient.getPerformance().getTimeout()))
        .baseUrl(Optional.ofNullable(restClient.getRequest().getEndpoint()).orElseThrow(EmptyBaseUrlException::new));

    retrofitBuilder
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(JacksonConverterFactory.create(JacksonFactory.create()))
        .validateEagerly(true);

    return retrofitBuilder.build().create(RepositoryClass);
  }

  private static OkHttpClient createOkHttpClient(OkHttpClient.Builder okHttpClient, TimeoutLevel timeoutLevel) {
    OkHttpClient.Builder builder = Optional.ofNullable(okHttpClient).orElseGet(OkHttpClient.Builder::new);
    Optional.ofNullable(timeoutLevel.getConnectionTimeoutDuration()).ifPresent(builder::connectTimeout);
    Optional.ofNullable(timeoutLevel.getReadTimeoutDuration()).ifPresent(builder::readTimeout);
    Optional.ofNullable(timeoutLevel.getWriteTimeoutDuration()).ifPresent(builder::writeTimeout);
    return builder.build();
  }
}