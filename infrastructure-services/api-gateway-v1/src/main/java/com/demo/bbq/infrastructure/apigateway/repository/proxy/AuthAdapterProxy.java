package com.demo.bbq.infrastructure.apigateway.repository.proxy;

import io.reactivex.Observable;
import java.util.HashMap;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;

public interface AuthAdapterProxy {

  @Headers({"Accept: application/json"})
  @GET("roles")
  Observable<HashMap<String, Integer>> listRoles(@Header("Authorization") String authHeader);
}
