package com.demo.poc.entrypoint.auth.repository.authprovider;

import com.demo.poc.commons.core.properties.restclient.RestClient;
import com.demo.poc.commons.custom.properties.ApplicationProperties;
import com.demo.poc.commons.core.restclient.RetrofitFactory;
import com.demo.poc.commons.core.restclient.utils.HeadersFiller;
import com.demo.poc.entrypoint.auth.repository.authprovider.wrapper.UserInfoResponseWrapper;
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
    RestClient restClient = properties.searchRestClient(USER_INFO_SERVICE_NAME);
    Map<String, String> addedHeaders = HeadersFiller.fillHeaders(restClient.getRequest().getHeaders(), currentHeaders);
    return getUserInfo(addedHeaders);
  }

  @Configuration
  class AuthTokenRepositoryConfig {

    @Bean(USER_INFO_SERVICE_NAME)
    UserInfoRepository create(OkHttpClient.Builder builder, ApplicationProperties properties) {
      RestClient restClient = properties.searchRestClient(USER_INFO_SERVICE_NAME);
      return RetrofitFactory.create(builder, restClient, UserInfoRepository.class);
    }
  }
}
