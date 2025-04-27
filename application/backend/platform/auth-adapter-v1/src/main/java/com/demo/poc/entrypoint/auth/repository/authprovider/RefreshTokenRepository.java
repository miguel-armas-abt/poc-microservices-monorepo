package com.demo.poc.entrypoint.auth.repository.authprovider;

import com.demo.poc.commons.core.properties.restclient.RestClient;
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

public interface RefreshTokenRepository {

  String REFRESH_TOKEN_SERVICE_NAME = "refresh-token";

  @FormUrlEncoded
  @POST("token")
  Single<TokenResponseWrapper> refreshToken(@FieldMap Map<String, String> formData,
                                            @HeaderMap Map<String, String> headers);

  default Single<TokenResponseWrapper> refreshToken(Map<String, String> currentHeaders,
                                                    ApplicationProperties properties,
                                                    String refreshToken) {
    RestClient restClient = properties.searchRestClient(REFRESH_TOKEN_SERVICE_NAME);
    Map<String, String> formData = fillFormData(restClient, refreshToken);
    Map<String, String> addedHeaders = HeadersFiller.fillHeaders(restClient.getRequest().getHeaders(), currentHeaders);
    return refreshToken(formData, addedHeaders);
  }

  static Map<String, String> fillFormData(RestClient restClient, String refreshToken) {
    Map<String, String> providedParams = restClient.getRequest().getFormData();
    Map<String, String> currentParams = Map.of("refresh_token", refreshToken);
    return FormDataFiller.fillFormData(providedParams, currentParams);
  }

  @Configuration
  class AuthTokenRepositoryConfig {

    @Bean(REFRESH_TOKEN_SERVICE_NAME)
    RefreshTokenRepository create(OkHttpClient.Builder builder, ApplicationProperties properties) {
      RestClient restClient = properties.searchRestClient(REFRESH_TOKEN_SERVICE_NAME);
      return RetrofitFactory.create(builder, restClient, RefreshTokenRepository.class);
    }
  }
}
