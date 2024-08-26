package com.demo.bbq.entrypoint.auth.repository.authprovider;

import com.demo.bbq.commons.properties.ApplicationProperties;
import com.demo.bbq.commons.restclient.retrofit.RetrofitFactory;
import com.demo.bbq.entrypoint.auth.repository.authprovider.wrapper.UserInfoResponseWrapper;
import io.reactivex.rxjava3.core.Single;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface UserInfoRepository {

  String USER_INFO_SERVICE_NAME = "user-info";

  @POST("userinfo")
  Single<UserInfoResponseWrapper> getUserInfo(@Header("Authorization") String token);

  @Configuration
  class AuthTokenRepositoryConfig {

    @Bean(USER_INFO_SERVICE_NAME)
    UserInfoRepository create(OkHttpClient.Builder builder, ApplicationProperties properties) {
      return RetrofitFactory.create(builder, properties, USER_INFO_SERVICE_NAME, UserInfoRepository.class);
    }
  }
}
