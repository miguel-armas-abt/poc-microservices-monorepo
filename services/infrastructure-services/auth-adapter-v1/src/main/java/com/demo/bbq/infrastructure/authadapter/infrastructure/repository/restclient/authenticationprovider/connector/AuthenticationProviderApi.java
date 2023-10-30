package com.demo.bbq.infrastructure.authadapter.infrastructure.repository.restclient.authenticationprovider.connector;

import com.demo.bbq.infrastructure.authadapter.infrastructure.repository.restclient.authenticationprovider.connector.dto.TokenResponse;
import com.demo.bbq.infrastructure.authadapter.infrastructure.repository.restclient.authenticationprovider.connector.dto.UserInfoResponse;
import io.reactivex.Completable;
import io.reactivex.Single;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface AuthenticationProviderApi {

  @FormUrlEncoded
  @POST("token")
  Single<TokenResponse> getToken(
      @Field("username") String username,
      @Field("password") String password,
      @Field("client_id") String clientId,
      @Field("grant_type") String grantType,
      @Field("client_secret") String clientSecret,
      @Field("scope") String scope);

  @FormUrlEncoded
  @POST("token")
  Single<TokenResponse> refresh(
      @Field("client_id") String clientId,
      @Field("grant_type") String grantType,
      @Field("refresh_token") String refreshToken);

  @POST("userinfo")
  Single<UserInfoResponse> getUserInfo(@Header("Authorization") String token);

  @FormUrlEncoded
  @POST("logout")
  Completable logout(
      @Field("client_id") String clientId,
      @Field("client_secret") String clientSecret,
      @Field("refresh_token") String refreshToken);
}
