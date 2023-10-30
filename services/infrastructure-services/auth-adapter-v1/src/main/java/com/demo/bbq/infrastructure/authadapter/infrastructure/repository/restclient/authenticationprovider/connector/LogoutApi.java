package com.demo.bbq.infrastructure.authadapter.infrastructure.repository.restclient.authenticationprovider.connector;

import io.reactivex.Completable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface LogoutApi {

  @FormUrlEncoded
  @POST("logout")
  Completable logout(
      @Field("client_id") String clientId,
      @Field("client_secret") String clientSecret,
      @Field("refresh_token") String refreshToken);
}
