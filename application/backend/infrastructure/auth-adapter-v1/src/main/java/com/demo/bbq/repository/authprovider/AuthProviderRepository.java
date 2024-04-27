package com.demo.bbq.repository.authprovider;

import com.demo.bbq.repository.authprovider.wrapper.TokenResponseWrapper;
import com.demo.bbq.repository.authprovider.wrapper.UserInfoResponseWrapper;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface AuthProviderRepository {

  @FormUrlEncoded
  @POST("token")
  Single<TokenResponseWrapper> getToken(
      @Field("username") String username,
      @Field("password") String password,
      @Field("client_id") String clientId,
      @Field("grant_type") String grantType,
      @Field("client_secret") String clientSecret,
      @Field("scope") String scope);

  @FormUrlEncoded
  @POST("token")
  Single<TokenResponseWrapper> refresh(
      @Field("client_id") String clientId,
      @Field("grant_type") String grantType,
      @Field("refresh_token") String refreshToken);

  @POST("userinfo")
  Single<UserInfoResponseWrapper> getUserInfo(@Header("Authorization") String token);

  @FormUrlEncoded
  @POST("logout")
  Completable logout(
      @Field("client_id") String clientId,
      @Field("client_secret") String clientSecret,
      @Field("refresh_token") String refreshToken);
}
