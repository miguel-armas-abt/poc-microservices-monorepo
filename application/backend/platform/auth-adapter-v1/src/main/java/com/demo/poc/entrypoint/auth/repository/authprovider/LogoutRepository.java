package com.demo.poc.entrypoint.auth.repository.authprovider;

import com.demo.poc.commons.core.properties.restclient.RestClient;
import com.demo.poc.commons.custom.properties.ApplicationProperties;
import com.demo.poc.commons.core.restclient.RetrofitFactory;
import com.demo.poc.commons.core.restclient.utils.FormDataFiller;
import com.demo.poc.commons.core.restclient.utils.HeadersFiller;
import io.reactivex.rxjava3.core.Completable;
import java.util.Map;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;

public interface LogoutRepository {

  String LOGOUT_SERVICE_NAME = "logout";

  @FormUrlEncoded
  @POST("logout")
  Completable logout(@FieldMap Map<String, String> formData,
                     @HeaderMap Map<String, String> headers);

  default Completable logout(Map<String, String> currentHeaders, ApplicationProperties properties, String refreshToken) {
    RestClient restClient = properties.searchRestClient(LOGOUT_SERVICE_NAME);
    Map<String, String> formData = fillFormData(restClient, refreshToken);
    Map<String, String> addedHeaders = HeadersFiller.fillHeaders(restClient.getRequest().getHeaders(), currentHeaders);
    return logout(formData, addedHeaders);
  }

  static Map<String, String> fillFormData(RestClient restClient, String refreshToken) {
    Map<String, String> providedParams = restClient.getRequest().getFormData();
    Map<String, String> currentParams = Map.of("refresh_token", refreshToken);
    return FormDataFiller.fillFormData(providedParams, currentParams);
  }

  @Configuration
  class AuthTokenRepositoryConfig {

    @Bean(LOGOUT_SERVICE_NAME)
    LogoutRepository create(OkHttpClient.Builder builder, ApplicationProperties properties) {
      RestClient restClient = properties.searchRestClient(LOGOUT_SERVICE_NAME);
      return RetrofitFactory.create(builder, restClient, LogoutRepository.class);
    }
  }
}
