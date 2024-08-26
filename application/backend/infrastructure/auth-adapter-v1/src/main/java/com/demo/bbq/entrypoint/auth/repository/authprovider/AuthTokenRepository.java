package com.demo.bbq.entrypoint.auth.repository.authprovider;

import com.demo.bbq.commons.properties.ApplicationProperties;
import com.demo.bbq.commons.restclient.retrofit.RetrofitFactory;
import com.demo.bbq.commons.toolkit.params.filler.FormDataFiller;
import com.demo.bbq.entrypoint.auth.repository.authprovider.wrapper.TokenResponseWrapper;
import io.reactivex.rxjava3.core.Single;
import java.util.Map;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface AuthTokenRepository {

  String AUTH_TOKEN_SERVICE_NAME = "get-token";

  @FormUrlEncoded
  @POST("token")
  Single<TokenResponseWrapper> getToken(@FieldMap Map<String, String> formData);

  default Single<TokenResponseWrapper> getToken(ApplicationProperties properties,
                                                String username, String password) {
    Map<String, String> providedParams = properties.searchFormData(AUTH_TOKEN_SERVICE_NAME);
    Map<String, String> currentParams = Map.of("username", username, "password", password);
    Map<String, String> formData = FormDataFiller.fillFormData(providedParams, currentParams);
    return getToken(formData);
  }

  @Configuration
  class AuthTokenRepositoryConfig {

    @Bean(AUTH_TOKEN_SERVICE_NAME)
    AuthTokenRepository create(OkHttpClient.Builder builder, ApplicationProperties properties) {
      return RetrofitFactory.create(builder, properties, AUTH_TOKEN_SERVICE_NAME, AuthTokenRepository.class);
    }
  }
}
