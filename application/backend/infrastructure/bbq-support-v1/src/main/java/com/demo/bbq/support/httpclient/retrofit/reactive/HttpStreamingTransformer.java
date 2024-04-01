package com.demo.bbq.support.httpclient.retrofit.reactive;

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
public interface HttpStreamingTransformer<T>
    extends ObservableTransformer<ResponseBody, T>, FlowableTransformer<ResponseBody, T> {

  public static final ObjectMapper mapper = JacksonMapperFactory.newObjectMapper();

  static <E> HttpStreamingTransformer<E> of(Class<E> tdClass) {
    return () -> tdClass;
  }

  default Publisher<T> apply(Flowable<ResponseBody> source) {
    return source.flatMap(body -> Flowable.create(emitter ->
      this.fetchStreamingResponse(body, emitter), BackpressureStrategy.BUFFER));
  }

  default ObservableSource<T> apply(Observable<ResponseBody> observable) {
    return observable.flatMap(body -> Observable.create(emitter ->
      this.fetchStreamingResponse(body, emitter)));
  }

  private void fetchStreamingResponse(ResponseBody body, Emitter<T> emitter) {
    try {
      BufferedSource is = body.source();
      try {
        while (true) {
          if (is.exhausted()) {
            emitter.onComplete();
            break;
          }
          String data = is.readUtf8Line();
          emitter.onNext(mapper.readValue(data, getTarget()));
        }
      } catch (Throwable var7) {
        if (is != null)
          try {
            is.close();
          } catch (Throwable var6) {
            var7.addSuppressed(var6);
          }
        throw var7;
      }
      if (is != null)
        is.close();
    } catch (Exception var8) {
      emitter.onError(var8);
    }
  }

  Class<T> getTarget();
}
