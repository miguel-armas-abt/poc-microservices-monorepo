package com.demo.poc.entrypoint.auth.repository.authprovider;

import com.demo.poc.commons.custom.properties.ApplicationProperties;
import com.demo.poc.commons.core.restclient.RetrofitFactory;
import com.demo.poc.commons.core.restclient.utils.FormDataFiller;
import com.demo.poc.commons.core.restclient.utils.HeadersFiller;
import com.demo.poc.entrypoint.auth.repository.authprovider.wrapper.TokenResponseWrapper;
import io.reactivex.rxjava3.core.Single;
import java.util.Map;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;

public interface AuthTokenRepository {

  String AUTH_TOKEN_SERVICE_NAME = "auth-token";

  @FormUrlEncoded
  @POST("token")
  Single<TokenResponseWrapper> getToken(@FieldMap Map<String, String> formData,
                                        @HeaderMap Map<String, String> headers);

  default Single<TokenResponseWrapper> getToken(Map<String, String> currentHeaders,
                                                ApplicationProperties properties,
                                                String username, String password) {

    Map<String, String> formData = fillFormData(properties, username, password);
    Map<String, String> addedHeaders = HeadersFiller.fillHeaders(properties.searchHeaders(AUTH_TOKEN_SERVICE_NAME), currentHeaders);
    return getToken(formData, addedHeaders);
  }

  static Map<String, String> fillFormData(ApplicationProperties properties, String username, String password) {
    Map<String, String> providedParams = properties.searchFormData(AUTH_TOKEN_SERVICE_NAME);
    Map<String, String> currentParams = Map.of("username", username, "password", password);
    return FormDataFiller.fillFormData(providedParams, currentParams);
  }

  @Configuration
  class AuthTokenRepositoryConfig {

    @Bean(AUTH_TOKEN_SERVICE_NAME)
    AuthTokenRepository create(OkHttpClient.Builder builder, ApplicationProperties properties) {
      return RetrofitFactory.create(builder, properties, AUTH_TOKEN_SERVICE_NAME, AuthTokenRepository.class);
    }
  }
}
