package com.demo.bbq.infrastructure.apigateway.infrastructure.config.restclient;

import static io.netty.channel.ChannelOption.CONNECT_TIMEOUT_MILLIS;
import static io.netty.handler.ssl.util.InsecureTrustManagerFactory.INSTANCE;

import com.demo.bbq.support.exception.model.ApiException;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import javax.net.ssl.SSLException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;
import reactor.netty.transport.logging.AdvancedByteBufFormat;

@Slf4j
@Component
public class WebClientFactory {

  public WebClient createWebClient() {
    Pool pool = new Pool();
    Timeout timeout = new Timeout();

    try {
      HttpClient httpClient = buildHttpClient(timeout, pool);
      return WebClient.builder()
          .clientConnector(new ReactorClientHttpConnector(httpClient))
//          .filter(this.loggingRequestExchangedFilter)
//          .filter(this.loggingResponseExchangeFilter)
          .build();
    } catch (SSLException ex) {
      throw ApiException.builder()
          .cause(ex)
          .message("SSL error during WebClient creation")
          .build();
    }
  }

  private HttpClient buildHttpClient(Timeout timeout, Pool pool) throws SSLException {
    SslContext sslContext = buildSslContext();
    HttpClient httpClient = HttpClient
        .create(buildConnectionProvider(pool))
        .secure(sslContextSpec -> sslContextSpec.sslContext(sslContext))
        .option(CONNECT_TIMEOUT_MILLIS, (int) timeout.getConnection().toMillis())
        .doOnConnected(connection -> connection
            .addHandlerLast(new ReadTimeoutHandler((int) timeout.getResponse().getSeconds()))
            .addHandlerLast(new WriteTimeoutHandler((int) timeout.getRequest().getSeconds())));
    if (log.isDebugEnabled()) httpClient = httpClient.wiretap(HttpClient.class.getCanonicalName(), LogLevel.DEBUG, AdvancedByteBufFormat.TEXTUAL);

    return httpClient;
  }

  private SslContext buildSslContext() throws SSLException {
    return SslContextBuilder.forClient()
        .trustManager(INSTANCE)
        .build();
  }

  private ConnectionProvider buildConnectionProvider(Pool pool) {
    return ConnectionProvider.builder(pool.getName())
        .maxConnections(pool.getSize())
        .pendingAcquireMaxCount(pool.getQueue())
        .pendingAcquireTimeout(pool.getQueueTimeout())
        .build();
  }
}
