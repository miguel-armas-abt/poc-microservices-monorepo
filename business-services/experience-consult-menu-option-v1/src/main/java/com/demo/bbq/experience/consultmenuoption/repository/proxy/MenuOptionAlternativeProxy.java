package com.demo.bbq.experience.consultmenuoption.repository.proxy;

import com.demo.bbq.experience.consultmenuoption.util.model.dto.request.MenuOptionRequest;
import com.demo.bbq.experience.consultmenuoption.util.model.dto.thirdparty.MenuOptionThird;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import io.reactivex.Observable;
import lombok.RequiredArgsConstructor;
import okhttp3.ResponseBody;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.adapter.rxjava.RxJava2Adapter;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;

@Component
@RequiredArgsConstructor
public class MenuOptionAlternativeProxy {

  private final WebClient.Builder webClientBuilder;

  @Value("${application.http-client.menu-options.base-url}")
  private String menuOptionsBaseUrl;

  TcpClient tcpClient = TcpClient
      .create()
      .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
      .doOnConnected(connection -> {
        connection.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS));
        connection.addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS));
      });

  private WebClient buildWebClient() {
    return webClientBuilder.clientConnector(new ReactorClientHttpConnector(HttpClient.from(tcpClient)))
        .baseUrl(menuOptionsBaseUrl)
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .defaultUriVariables(Collections.singletonMap("url", menuOptionsBaseUrl))
        .build();
  }

  public Observable<List<MenuOptionThird>> findByCategory(String category) {
    return RxJava2Adapter.fluxToObservable(buildWebClient().method(HttpMethod.GET)
          .uri(uriBuilder -> (category != null)
                  ? uriBuilder.path("menu-options").queryParam("category", category).build()
                  : uriBuilder.path("menu-options").build())
          .retrieve()
          .bodyToFlux(MenuOptionThird.class)
          .collectList()
          .flux());
  }

  public Observable<MenuOptionThird> findById(Long id) {
    return RxJava2Adapter.fluxToObservable(buildWebClient().method(HttpMethod.GET)
        .uri(uriBuilder -> uriBuilder.path("menu-options/" + id).build())
        .retrieve()
        .bodyToFlux(MenuOptionThird.class));
  }

  //error to cast response body
  public Observable<ResponseBody> save(MenuOptionRequest menuOption) {
    return RxJava2Adapter.fluxToObservable(buildWebClient().method(HttpMethod.POST)
        .uri(uriBuilder -> uriBuilder.path("menu-options").build())
        .contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromObject(menuOption))
        .retrieve()
        .bodyToFlux(ResponseBody.class));
  }

  //error to cast response body
  public Observable<ResponseBody> update(Long id, MenuOptionRequest menuOption) {
    return RxJava2Adapter.fluxToObservable(buildWebClient().method(HttpMethod.POST)
        .uri(uriBuilder -> uriBuilder.path("menu-options/" + id).build())
        .contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromObject(menuOption))
        .retrieve()
        .bodyToFlux(ResponseBody.class));
  }

  //error to cast response body
  public Observable<ResponseBody> delete(Long id) {
    return RxJava2Adapter.fluxToObservable(buildWebClient().method(HttpMethod.POST)
        .uri(uriBuilder -> uriBuilder.path("menu-options/" + id).build())
        .retrieve()
        .bodyToFlux(ResponseBody.class));
  }
}
