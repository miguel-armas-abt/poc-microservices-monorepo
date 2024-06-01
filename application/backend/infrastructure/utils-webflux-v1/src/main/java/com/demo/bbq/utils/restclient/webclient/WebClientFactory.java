package com.demo.bbq.utils.restclient.webclient;

import static io.netty.channel.ChannelOption.CONNECT_TIMEOUT_MILLIS;
import static io.netty.handler.ssl.util.InsecureTrustManagerFactory.INSTANCE;

import com.demo.bbq.utils.restclient.webclient.dto.Pool;
import com.demo.bbq.utils.restclient.webclient.dto.Timeout;
import com.demo.bbq.utils.errors.exceptions.SystemException;
import io.micrometer.observation.ObservationRegistry;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import java.util.List;
import javax.net.ssl.SSLException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.DefaultClientRequestObservationConvention;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;
import reactor.netty.transport.logging.AdvancedByteBufFormat;

@Slf4j
public class WebClientFactory {

  public static WebClient createWebClient(List<ExchangeFilterFunction> filters,
                                          ObservationRegistry observationRegistry) {
    Pool pool = new Pool();
    Timeout timeout = new Timeout();

    try {
      HttpClient httpClient = buildHttpClient(timeout, pool);
      return WebClient.builder()
          .clientConnector(new ReactorClientHttpConnector(httpClient))
          .filters(extraFilters -> extraFilters.addAll(filters))
          .observationRegistry(observationRegistry)
          .observationConvention(new DefaultClientRequestObservationConvention())
          .build();
    } catch (SSLException ex) {
      throw new SystemException("SSLWebclientError");
    }
  }

  private static HttpClient buildHttpClient(Timeout timeout, Pool pool) throws SSLException {
    SslContext sslContext = buildSslContext();
    HttpClient httpClient = HttpClient
        .create(buildConnectionProvider(pool))
        .secure(sslContextSpec -> sslContextSpec.sslContext(sslContext))
        .option(CONNECT_TIMEOUT_MILLIS, (int) timeout.getConnection().toMillis())
        .doOnConnected(connection -> connection
            .addHandlerLast(new ReadTimeoutHandler((int) timeout.getResponse().getSeconds()))
            .addHandlerLast(new WriteTimeoutHandler((int) timeout.getRequest().getSeconds())));

    if (log.isDebugEnabled())
      httpClient = httpClient.wiretap(HttpClient.class.getCanonicalName(), LogLevel.DEBUG, AdvancedByteBufFormat.TEXTUAL);

    return httpClient;
  }

  private static SslContext buildSslContext() throws SSLException {
    return SslContextBuilder.forClient()
        .trustManager(INSTANCE)
        .build();
  }

  private static ConnectionProvider buildConnectionProvider(Pool pool) {
    return ConnectionProvider.builder(pool.getName())
        .maxConnections(pool.getSize())
        .pendingAcquireMaxCount(pool.getQueue())
        .pendingAcquireTimeout(pool.getQueueTimeout())
        .build();
  }
}
