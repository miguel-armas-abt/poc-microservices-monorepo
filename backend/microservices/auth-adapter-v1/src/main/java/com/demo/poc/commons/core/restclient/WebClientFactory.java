package com.demo.poc.commons.core.restclient;

import com.demo.poc.commons.core.errors.exceptions.UnexpectedSslException;
import com.demo.poc.commons.core.properties.restclient.PerformanceTemplate;
import com.demo.poc.commons.core.restclient.enums.ConcurrencyLevel;
import io.micrometer.observation.ObservationRegistry;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.DefaultClientRequestObservationConvention;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;
import reactor.netty.transport.logging.AdvancedByteBufFormat;

import javax.net.ssl.SSLException;
import java.util.List;

import static io.netty.channel.ChannelOption.CONNECT_TIMEOUT_MILLIS;
import static io.netty.handler.ssl.util.InsecureTrustManagerFactory.INSTANCE;

@Slf4j
@RequiredArgsConstructor
public class WebClientFactory {

  private final List<ExchangeFilterFunction> filters;
  private final ObservationRegistry observationRegistry;

  public WebClient createWebClient(PerformanceTemplate performance, String restClientName) {
    try {
      HttpClient httpClient = buildHttpClient(performance, restClientName);
      return WebClient.builder()
          .clientConnector(new ReactorClientHttpConnector(httpClient))
          .filters(extraFilters -> extraFilters.addAll(filters))
          .observationRegistry(observationRegistry)
          .observationConvention(new DefaultClientRequestObservationConvention())
          .build();
    } catch (SSLException ex) {
      throw new UnexpectedSslException(ex.getMessage());
    }
  }

  private static HttpClient buildHttpClient(PerformanceTemplate performance, String restClientName) throws SSLException {
    SslContext sslContext = buildSslContext();
    HttpClient httpClient = HttpClient
        .create(buildConnectionProvider(performance.getConcurrency(), restClientName))
        .secure(sslContextSpec -> sslContextSpec.sslContext(sslContext))
        .option(CONNECT_TIMEOUT_MILLIS, (int) performance.getTimeout().getConnectionTimeoutDuration().toMillis())
        .doOnConnected(connection -> connection
            .addHandlerLast(new ReadTimeoutHandler((int) performance.getTimeout().getReadTimeoutDuration().getSeconds()))
            .addHandlerLast(new WriteTimeoutHandler((int) performance.getTimeout().getWriteTimeoutDuration().getSeconds())));

    if (log.isDebugEnabled())
      httpClient = httpClient.wiretap(HttpClient.class.getCanonicalName(), LogLevel.DEBUG, AdvancedByteBufFormat.TEXTUAL);

    return httpClient;
  }

  private static SslContext buildSslContext() throws SSLException {
    return SslContextBuilder.forClient()
        .trustManager(INSTANCE)
        .build();
  }

  private static ConnectionProvider buildConnectionProvider(ConcurrencyLevel concurrencyLevel, String restClientName) {
    return ConnectionProvider.builder(restClientName)
        .maxConnections(concurrencyLevel.getMaxConnections())
        .pendingAcquireMaxCount(concurrencyLevel.getMaxConnections())
        .pendingAcquireTimeout(concurrencyLevel.getQueueTimeoutDuration())
        .build();
  }
}