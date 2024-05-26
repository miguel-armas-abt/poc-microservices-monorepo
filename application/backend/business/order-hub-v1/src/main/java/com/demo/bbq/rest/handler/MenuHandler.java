package com.demo.bbq.rest.handler;

import com.demo.bbq.repository.menu.MenuRepositoryStrategy;
import com.demo.bbq.repository.menu.wrapper.response.MenuOptionResponseWrapper;
import com.demo.bbq.utils.errors.exceptions.BusinessException;
import com.demo.bbq.utils.toolkit.ServerResponseBuilderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class MenuHandler {

  private final MenuRepositoryStrategy menuRepository;

  public Mono<ServerResponse> findMenuByCategory(ServerRequest serverRequest) {
    String category = serverRequest.queryParam("category")
        .orElseThrow(() -> new BusinessException("Invalid category"));

    return ServerResponseBuilderUtil
        .buildFlux(
            ServerResponse.ok(),
            serverRequest.headers(),
            MenuOptionResponseWrapper.class,
            menuRepository.getService()
                .findByCategory(serverRequest, category)
        );
  }
}
