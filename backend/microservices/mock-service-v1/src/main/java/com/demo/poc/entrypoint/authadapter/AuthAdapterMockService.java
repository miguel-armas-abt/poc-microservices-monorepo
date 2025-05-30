package com.demo.poc.entrypoint.authadapter;

import static com.demo.poc.commons.utils.DelayGenerator.generateRandomDelay;
import static com.demo.poc.commons.utils.HeadersGenerator.contentType;
import static com.demo.poc.commons.utils.HeadersGenerator.generateTraceId;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static com.demo.poc.commons.utils.JsonReader.readJSON;

import java.util.concurrent.TimeUnit;
import org.apache.http.entity.ContentType;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.Header;
import org.mockserver.model.HttpStatusCode;
import org.springframework.stereotype.Component;
import com.demo.poc.commons.config.MockService;

@Component
public class AuthAdapterMockService implements MockService {

  @Override
  public void loadMocks(ClientAndServer mockServer) {

    mockServer
        .when(request()
            .withMethod("GET")
            .withPath("/poc/platform/auth-adapter/v1/auth/roles"))
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
