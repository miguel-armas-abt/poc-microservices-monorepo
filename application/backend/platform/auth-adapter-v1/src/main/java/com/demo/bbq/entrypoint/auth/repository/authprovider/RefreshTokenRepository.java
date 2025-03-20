package com.demo.bbq.entrypoint.auth.repository.authprovider;

import com.demo.bbq.commons.properties.ApplicationProperties;
import com.demo.bbq.commons.restclient.RetrofitFactory;
import com.demo.bbq.commons.restclient.utils.FormDataFiller;
import com.demo.bbq.commons.restclient.utils.HeadersFiller;
import com.demo.bbq.entrypoint.auth.repository.authprovider.wrapper.TokenResponseWrapper;
import io.reactivex.rxjava3.core.Single;
import java.util.Map;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;

public interface RefreshTokenRepository {

  String REFRESH_TOKEN_SERVICE_NAME = "refresh-token";

  @FormUrlEncoded
  @POST("token")
  Single<TokenResponseWrapper> refreshToken(@FieldMap Map<String, String> formData,
                                            @HeaderMap Map<String, String> headers);

  default Single<TokenResponseWrapper> refreshToken(Map<String, String> currentHeaders,
                                                    ApplicationProperties properties,
                                                    String refreshToken) {
    Map<String, String> formData = fillFormData(properties, refreshToken);
    Map<String, String> addedHeaders = HeadersFiller.fillHeaders(properties.searchHeaders(REFRESH_TOKEN_SERVICE_NAME), currentHeaders);
    return refreshToken(formData, addedHeaders);
  }

  static Map<String, String> fillFormData(ApplicationProperties properties, String refreshToken) {
    Map<String, String> providedParams = properties.searchFormData(REFRESH_TOKEN_SERVICE_NAME);
    Map<String, String> currentParams = Map.of("refresh_token", refreshToken);
    return FormDataFiller.fillFormData(providedParams, currentParams);
  }

  @Configuration
  class AuthTokenRepositoryConfig {

    @Bean(REFRESH_TOKEN_SERVICE_NAME)
    RefreshTokenRepository create(OkHttpClient.Builder builder, ApplicationProperties properties) {
      return RetrofitFactory.create(builder, properties, REFRESH_TOKEN_SERVICE_NAME, RefreshTokenRepository.class);
    }
  }
}
