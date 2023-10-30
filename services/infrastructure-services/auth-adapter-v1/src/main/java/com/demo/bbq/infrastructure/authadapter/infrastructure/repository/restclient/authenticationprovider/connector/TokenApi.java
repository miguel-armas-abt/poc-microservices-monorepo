package com.demo.bbq.infrastructure.authadapter.infrastructure.repository.restclient.authenticationprovider.connector;

import com.demo.bbq.infrastructure.authadapter.infrastructure.repository.restclient.authenticationprovider.connector.dto.TokenResponse;
import io.reactivex.Single;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface TokenApi {

  @FormUrlEncoded
  @POST("token")
  Single<TokenResponse> getToken(
      @Field("username") String username,
      @Field("password") String password,
      @Field("client_id") String clientId,
      @Field("grant_type") String grantType,
      @Field("client_secret") String clientSecret,
      @Field("scope") String scope);
}
