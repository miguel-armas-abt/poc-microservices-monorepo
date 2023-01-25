package com.demo.bbq.support.reactive.httpclient;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Emitter;
import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import okhttp3.ResponseBody;
import okio.BufferedSource;
import org.reactivestreams.Publisher;

@FunctionalInterface
@SuppressFBWarnings(value = {"RCN_REDUNDANT_NULLCHECK_OF_NONNULL_VALUE", "REC_CATCH_EXCEPTION", "UPM_UNCALLED_PRIVATE_METHOD"}, justification = "False positive JDK 11")
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
