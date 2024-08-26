package com.demo.bbq.entrypoint.auth.repository.authprovider;

import com.demo.bbq.commons.properties.ApplicationProperties;
import com.demo.bbq.commons.restclient.retrofit.RetrofitFactory;
import com.demo.bbq.commons.toolkit.params.filler.FormDataFiller;
import io.reactivex.rxjava3.core.Completable;
import java.util.Map;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface LogoutRepository {

  String LOGOUT_SERVICE_NAME = "logout";

  @FormUrlEncoded
  @POST("logout")
  Completable logout(@FieldMap Map<String, String> formData);

  default Completable logout(ApplicationProperties properties, String refreshToken) {
    Map<String, String> providedParams = properties.searchFormData(LOGOUT_SERVICE_NAME);
    Map<String, String> currentParams = Map.of("refresh_token", refreshToken);
    Map<String, String> formData = FormDataFiller.fillFormData(providedParams, currentParams);
    return logout(formData);
  }

  @Configuration
  class AuthTokenRepositoryConfig {

    @Bean(LOGOUT_SERVICE_NAME)
    LogoutRepository create(OkHttpClient.Builder builder, ApplicationProperties properties) {
      return RetrofitFactory.create(builder, properties, LOGOUT_SERVICE_NAME, LogoutRepository.class);
    }
  }
}
