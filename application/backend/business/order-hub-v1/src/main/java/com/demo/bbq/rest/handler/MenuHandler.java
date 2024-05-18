package com.demo.bbq.rest.handler;

import com.demo.bbq.repository.menu.MenuRepositoryStrategy;
import com.demo.bbq.repository.menu.wrapper.response.MenuOptionResponseWrapper;
import com.demo.bbq.rest.common.BuilderServerResponse;
import com.demo.bbq.utils.errors.exceptions.BusinessException;
import com.newrelic.api.agent.Trace;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class MenuHandler {

  private final MenuRepositoryStrategy menuRepository;
  private final BuilderServerResponse<MenuOptionResponseWrapper> builderServerResponse;

  @Trace(dispatcher = true)
  public Mono<ServerResponse> findMenuByCategory(ServerRequest serverRequest) {
    String category = serverRequest.queryParam("category")
        .orElseThrow(() -> new BusinessException("Invalid category"));

    Flux<MenuOptionResponseWrapper> streamResponse = menuRepository.getService()
        .findByCategory(serverRequest, category);

    return builderServerResponse.buildStream(streamResponse, MenuOptionResponseWrapper.class);
  }
}
