package com.demo.bbq.infrastructure.apigateway.infrastructure.repository.restclient.authadapter;

import io.reactivex.Observable;
import java.util.HashMap;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;

public interface AuthAdapterApi {

  @Headers({"Accept: application/json"})
  @GET("roles")
  Observable<HashMap<String, Integer>> getRoles(@Header("Authorization") String authToken);
}
