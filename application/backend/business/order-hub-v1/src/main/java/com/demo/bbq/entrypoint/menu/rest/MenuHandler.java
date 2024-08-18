package com.demo.bbq.entrypoint.menu.rest;

import static com.demo.bbq.commons.toolkit.params.filler.HeadersFiller.extractHeadersAsMap;
import static com.demo.bbq.commons.toolkit.params.filler.QueryParamFiller.extractQueryParamsAsMap;

import com.demo.bbq.commons.toolkit.router.ServerResponseFactory;
import com.demo.bbq.commons.toolkit.validator.headers.DefaultHeaders;
import com.demo.bbq.commons.toolkit.validator.params.ParamValidator;
import com.demo.bbq.entrypoint.menu.params.pojo.CategoryParam;
import com.demo.bbq.entrypoint.menu.repository.MenuRepositoryStrategy;
import com.demo.bbq.entrypoint.menu.repository.wrapper.response.MenuOptionResponseWrapper;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class MenuHandler {

  private final MenuRepositoryStrategy menuRepository;
  private final ParamValidator paramValidator;

  public Mono<ServerResponse> findMenuByCategory(ServerRequest serverRequest) {
    Map<String, String> headers = extractHeadersAsMap(serverRequest);
    paramValidator.validate(headers, DefaultHeaders.class);
    CategoryParam categoryParam = paramValidator.validateAndRetrieve(extractQueryParamsAsMap(serverRequest), CategoryParam.class);

    return ServerResponseFactory
        .buildFlux(
            ServerResponse.ok(),
            serverRequest.headers(),
            MenuOptionResponseWrapper.class,
            menuRepository.getService().findByCategory(headers, categoryParam.getCategory())
        );
  }
}
