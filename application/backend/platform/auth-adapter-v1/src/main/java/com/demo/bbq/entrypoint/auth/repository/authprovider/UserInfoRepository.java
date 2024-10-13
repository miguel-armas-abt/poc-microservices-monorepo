package com.demo.bbq.entrypoint.auth.repository.authprovider;

import com.demo.bbq.commons.properties.ApplicationProperties;
import com.demo.bbq.commons.restclient.retrofit.RetrofitFactory;
import com.demo.bbq.commons.toolkit.params.filler.HeadersFiller;
import com.demo.bbq.entrypoint.auth.repository.authprovider.wrapper.UserInfoResponseWrapper;
import io.reactivex.rxjava3.core.Single;
import java.util.Map;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;

public interface UserInfoRepository {

  String USER_INFO_SERVICE_NAME = "user-info";

  @POST("userinfo")
  Single<UserInfoResponseWrapper> getUserInfo(@HeaderMap Map<String, String> headers);

  default Single<UserInfoResponseWrapper> getUserInfo(ApplicationProperties properties,
                                                      Map<String, String> currentHeaders) {
    Map<String, String> addedHeaders = HeadersFiller.fillHeaders(properties.searchHeaders(USER_INFO_SERVICE_NAME), currentHeaders);
    return getUserInfo(addedHeaders);
  }

  @Configuration
  class AuthTokenRepositoryConfig {

    @Bean(USER_INFO_SERVICE_NAME)
    UserInfoRepository create(OkHttpClient.Builder builder, ApplicationProperties properties) {
      return RetrofitFactory.create(builder, properties, USER_INFO_SERVICE_NAME, UserInfoRepository.class);
    }
  }
}
