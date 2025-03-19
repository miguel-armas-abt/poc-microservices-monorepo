package com.demo.bbq.entrypoint.menu.rest;

import com.demo.bbq.commons.restserver.ServerResponseFactory;
import com.demo.bbq.commons.validations.headers.DefaultHeaders;
import com.demo.bbq.commons.validations.headers.HeaderValidator;
import com.demo.bbq.commons.validations.params.ParamValidator;
import com.demo.bbq.entrypoint.menu.dto.params.CategoryParam;
import com.demo.bbq.entrypoint.menu.repository.MenuRepositorySelector;
import com.demo.bbq.entrypoint.menu.repository.wrapper.response.MenuOptionResponseWrapper;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.demo.bbq.commons.restclient.utils.HttpHeadersFiller.extractHeadersAsMap;
import static com.demo.bbq.commons.restclient.utils.QueryParamFiller.extractQueryParamsAsMap;

@Component
@RequiredArgsConstructor
public class MenuHandler {

  private final MenuRepositorySelector menuRepository;
  private final HeaderValidator headerValidator;
  private final ParamValidator paramValidator;

  public Mono<ServerResponse> findMenuByCategory(ServerRequest serverRequest) {
    Map<String, String> headers = extractHeadersAsMap(serverRequest);
    headerValidator.validate(headers, DefaultHeaders.class);
    CategoryParam categoryParam = paramValidator.validateAndRetrieve(extractQueryParamsAsMap(serverRequest), CategoryParam.class);

    return ServerResponseFactory
        .buildFlux(
            ServerResponse.ok(),
            serverRequest.headers(),
            MenuOptionResponseWrapper.class,
            menuRepository.selectStrategy().findByCategory(headers, categoryParam.getCategory())
        );
  }
}
