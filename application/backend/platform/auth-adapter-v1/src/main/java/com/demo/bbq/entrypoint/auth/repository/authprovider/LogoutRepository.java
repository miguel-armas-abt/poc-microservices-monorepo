package com.demo.bbq.entrypoint.auth.repository.authprovider;

import com.demo.bbq.commons.custom.properties.ApplicationProperties;
import com.demo.bbq.commons.core.restclient.RetrofitFactory;
import com.demo.bbq.commons.core.restclient.utils.FormDataFiller;
import com.demo.bbq.commons.core.restclient.utils.HeadersFiller;
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
    Map<String, String> formData = fillFormData(properties, refreshToken);
    Map<String, String> addedHeaders = HeadersFiller.fillHeaders(properties.searchHeaders(LOGOUT_SERVICE_NAME), currentHeaders);
    return logout(formData, addedHeaders);
  }

  static Map<String, String> fillFormData(ApplicationProperties properties, String refreshToken) {
    Map<String, String> providedParams = properties.searchFormData(LOGOUT_SERVICE_NAME);
    Map<String, String> currentParams = Map.of("refresh_token", refreshToken);
    return FormDataFiller.fillFormData(providedParams, currentParams);
  }

  @Configuration
  class AuthTokenRepositoryConfig {

    @Bean(LOGOUT_SERVICE_NAME)
    LogoutRepository create(OkHttpClient.Builder builder, ApplicationProperties properties) {
      return RetrofitFactory.create(builder, properties, LOGOUT_SERVICE_NAME, LogoutRepository.class);
    }
  }
}
