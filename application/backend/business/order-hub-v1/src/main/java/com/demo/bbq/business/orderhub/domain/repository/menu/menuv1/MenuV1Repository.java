package com.demo.bbq.business.orderhub.domain.repository.menu.menuv1;

import com.demo.bbq.business.orderhub.domain.repository.menu.MenuRepository;
import com.demo.bbq.business.orderhub.domain.repository.menu.menuv1.config.MenuV1RestClientProperties;
import com.demo.bbq.business.orderhub.domain.repository.menu.wrapper.response.MenuOptionResponseWrapper;
import com.demo.bbq.business.orderhub.domain.repository.menu.wrapper.request.MenuOptionSaveRequestWrapper;
import com.demo.bbq.business.orderhub.domain.repository.menu.wrapper.request.MenuOptionUpdateRequestWrapper;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import java.util.Collections;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
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
public class MenuV1Repository implements MenuRepository {

  private final WebClient.Builder webClientBuilder;
  private final MenuV1RestClientProperties properties;

  TcpClient tcpClient = TcpClient
      .create()
      .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
      .doOnConnected(connection -> {
        connection.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS));
        connection.addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS));
      });

  private WebClient buildWebClient() {
    return webClientBuilder.clientConnector(new ReactorClientHttpConnector(HttpClient.from(tcpClient)))
        .baseUrl(properties.getBaseURL())
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .defaultUriVariables(Collections.singletonMap("url", properties.getBaseURL()))
        .build();
  }

  public Observable<MenuOptionResponseWrapper> findByCategory(String category) {
    return RxJava2Adapter.fluxToObservable(buildWebClient().method(HttpMethod.GET)
        .uri(uriBuilder -> (category != null)
            ? uriBuilder.path("menu-options").queryParam("category", category).build()
            : uriBuilder.path("menu-options").build())
        .retrieve()
        .bodyToFlux(MenuOptionResponseWrapper.class));
  }

  public Maybe<MenuOptionResponseWrapper> findByProductCode(String productCode) {
    return RxJava2Adapter.monoToMaybe(buildWebClient().method(HttpMethod.GET)
        .uri(uriBuilder -> uriBuilder.path("menu-options/" + productCode).build())
        .retrieve()
        .bodyToMono(MenuOptionResponseWrapper.class));
  }

  public Completable save(MenuOptionSaveRequestWrapper menuOption) {
    return RxJava2Adapter.monoToCompletable(buildWebClient().method(HttpMethod.POST)
        .uri(uriBuilder -> uriBuilder.path("menu-options").build())
        .contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromObject(menuOption))
        .retrieve()
        .bodyToMono(Void.class));
  }

  public Completable update(String productCode, MenuOptionUpdateRequestWrapper menuOption) {
    return RxJava2Adapter.monoToCompletable(buildWebClient().method(HttpMethod.PUT)
        .uri(uriBuilder -> uriBuilder.path("menu-options/" + productCode).build())
        .contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromObject(menuOption))
        .retrieve()
        .bodyToMono(Void.class));
  }

  public Completable delete(String productCode) {
    return RxJava2Adapter.monoToCompletable(buildWebClient().method(HttpMethod.DELETE)
        .uri(uriBuilder -> uriBuilder.path("menu-options/" + productCode).build())
        .retrieve()
        .bodyToMono(Void.class));
  }

  @Override
  public boolean supports(Class<?> selectedClass) {
    return this.getClass().isAssignableFrom(selectedClass);
  }
}
