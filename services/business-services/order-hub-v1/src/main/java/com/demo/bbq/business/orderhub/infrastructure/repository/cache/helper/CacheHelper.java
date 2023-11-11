package com.demo.bbq.business.orderhub.infrastructure.repository.cache.helper;

import com.google.gson.Gson;
import io.reactivex.Completable;
import io.reactivex.Observable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Component;
import reactor.adapter.rxjava.RxJava2Adapter;

@Component
public class CacheHelper<T> {
  @Qualifier("reactiveStringRedisTemplate")
  @Autowired
  private ReactiveRedisOperations<String, T> redisOperations;

  public Completable put(String hashName, String hashKey, T hashValue) {
    return RxJava2Adapter
        .monoToCompletable(redisOperations.<String, String>opsForHash().put(hashName, hashKey, new Gson().toJson(hashValue)));
  }

  public Observable<T> getAll(String hashName, Class<T> selectedClass) {
    return RxJava2Adapter
        .fluxToObservable(redisOperations.<String, String>opsForHash().values(hashName))
        .cast(String.class)
        .map(json -> new Gson().fromJson(json, selectedClass));
  }
}
