package com.demo.bbq.commons.restclient;

import com.demo.bbq.commons.errors.exceptions.SystemException;
import com.demo.bbq.commons.properties.base.ConfigurationBaseProperties;
import com.demo.bbq.commons.restclient.enums.TimeoutLevel;
import com.demo.bbq.commons.serialization.JacksonFactory;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.util.Optional;

public class RetrofitFactory {

  public static <T> T create(OkHttpClient.Builder okHttpClient,
                             ConfigurationBaseProperties properties,
                             String serviceName,
                             Class<T> RepositoryClass) {

    Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
        .client(createOkHttpClient(okHttpClient, properties.searchPerformance(serviceName).getTimeout()))
        .baseUrl(Optional.ofNullable(properties.searchEndpoint(serviceName)).orElseThrow(() -> new SystemException("Base URL is required")));

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