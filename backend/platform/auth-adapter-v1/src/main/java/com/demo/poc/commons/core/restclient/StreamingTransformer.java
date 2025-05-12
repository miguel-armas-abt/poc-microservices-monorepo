package com.demo.poc.commons.core.restclient;

import com.demo.poc.commons.core.serialization.JacksonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Emitter;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.FlowableTransformer;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.core.ObservableTransformer;
import okhttp3.ResponseBody;
import okio.BufferedSource;
import org.reactivestreams.Publisher;

@FunctionalInterface
public interface StreamingTransformer<T> extends ObservableTransformer<ResponseBody, T>, FlowableTransformer<ResponseBody, T> {

  ObjectMapper mapper = JacksonFactory.create();

  static <E> StreamingTransformer<E> of(Class<E> targetClass) {
    return () -> targetClass;
  }

  @Override
  default Publisher<T> apply(Flowable<ResponseBody> source) {
    return source.flatMap(body -> Flowable.create(emitter ->
        processStreamingData(body, emitter), BackpressureStrategy.BUFFER));
  }

  @Override
  default ObservableSource<T> apply(Observable<ResponseBody> observable) {
    return observable.flatMap(body -> Observable.create(emitter ->
        processStreamingData(body, emitter)));
  }

  private void processStreamingData(ResponseBody body, Emitter<T> emitter) {
    try (BufferedSource source = body.source()) {
      String line;
      while ((line = source.readUtf8Line()) != null) {
        T parsedData = mapper.readValue(line, getTarget());
        emitter.onNext(parsedData);
      }
      emitter.onComplete();
    } catch (Exception e) {
      emitter.onError(e);
    }
  }

  Class<T> getTarget();
}
