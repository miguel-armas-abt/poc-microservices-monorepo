package com.demo.poc.service.authadapter;

import static com.demo.poc.commons.toolkit.delay.DelayGenerator.generateRandomDelay;
import static com.demo.poc.commons.toolkit.headers.HeadersGenerator.contentType;
import static com.demo.poc.commons.toolkit.headers.HeadersGenerator.generateTraceId;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static com.demo.poc.commons.TemplateConfig.readJSON;

import java.util.concurrent.TimeUnit;
import org.apache.http.entity.ContentType;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.Header;
import org.mockserver.model.HttpStatusCode;
import org.springframework.stereotype.Component;
import com.demo.poc.commons.MockRuleProvider;

@Component
public class AuthAdapterServiceMock implements MockRuleProvider {

  @Override
  public void loadMocks(ClientAndServer mockServer) {

    mockServer
        .when(request()
            .withMethod("GET")
            .withPath("/poc/infrastructure/v1/auth/roles"))
        .respond(request -> {
          long randomDelay = generateRandomDelay();
          Header traceIdHeader = generateTraceId();
          return response()
              .withStatusCode(HttpStatusCode.BAD_REQUEST_400.code())
              .withHeader(contentType(ContentType.APPLICATION_JSON.getMimeType()))
              .withHeader(traceIdHeader)
              .withBody(readJSON("mocks/auth-adapter-v1/get-roles.400.json"))
              .withDelay(TimeUnit.MILLISECONDS, randomDelay);
        });
  }
}
