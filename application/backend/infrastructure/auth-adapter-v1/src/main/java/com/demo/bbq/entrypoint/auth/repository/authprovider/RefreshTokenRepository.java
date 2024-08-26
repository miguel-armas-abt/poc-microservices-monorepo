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

public interface RefreshTokenRepository {

  String REFRESH_TOKEN_SERVICE_NAME = "refresh-token";

  @FormUrlEncoded
  @POST("token")
  Single<TokenResponseWrapper> refreshToken(@FieldMap Map<String, String> formData);

  default Single<TokenResponseWrapper> refreshToken(ApplicationProperties properties, String refreshToken) {
    Map<String, String> providedParams = properties.searchFormData(REFRESH_TOKEN_SERVICE_NAME);
    Map<String, String> currentParams = Map.of("refresh_token", refreshToken);
    Map<String, String> formData = FormDataFiller.fillFormData(providedParams, currentParams);
    return refreshToken(formData);
  }

  @Configuration
  class AuthTokenRepositoryConfig {

    @Bean(REFRESH_TOKEN_SERVICE_NAME)
    RefreshTokenRepository create(OkHttpClient.Builder builder, ApplicationProperties properties) {
      return RetrofitFactory.create(builder, properties, REFRESH_TOKEN_SERVICE_NAME, RefreshTokenRepository.class);
    }
  }
}
