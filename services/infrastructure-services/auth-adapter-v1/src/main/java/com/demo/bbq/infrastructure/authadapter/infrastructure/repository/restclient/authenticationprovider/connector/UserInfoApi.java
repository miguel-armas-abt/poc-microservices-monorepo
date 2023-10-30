package com.demo.bbq.infrastructure.authadapter.infrastructure.repository.restclient.authenticationprovider.connector;

import com.demo.bbq.infrastructure.authadapter.infrastructure.repository.restclient.authenticationprovider.connector.dto.UserInfoResponse;
import io.reactivex.Single;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface UserInfoApi {

  @POST("userinfo")
  Single<UserInfoResponse> getUserInfo(@Header("Authorization") String token);
}
