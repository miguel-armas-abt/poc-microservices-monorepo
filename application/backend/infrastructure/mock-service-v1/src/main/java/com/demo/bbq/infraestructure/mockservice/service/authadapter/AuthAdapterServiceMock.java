package com.demo.bbq.infraestructure.mockservice.service.authadapter;

import static org.mockserver.model.Header.header;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static com.demo.bbq.infraestructure.mockservice.common.TemplateConfig.readJSON;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.Header;
import org.mockserver.model.HttpStatusCode;
import org.springframework.stereotype.Component;
import com.demo.bbq.infraestructure.mockservice.common.MockRuleProvider;

@Component
public class AuthAdapterServiceMock implements MockRuleProvider {

  private static final String CONTENT_TYPE_NAME = "Content-Type";
  private static final String CONTENT_TYPE_VALUE_JSON = "application/json";

  @Override
  public void loadMocks(ClientAndServer mockServer) {

    mockServer
        .when(request()
            .withMethod("GET")
            .withPath("/bbq/infrastructure/v1/auth/roles"))
        .respond(request -> {
          long randomDelay = buildDelayWithProbabilityDensity();
          Header traceIdHeader = buildTraceId();
          return response()
              .withStatusCode(HttpStatusCode.BAD_REQUEST_400.code())
              .withHeader(header(CONTENT_TYPE_NAME, CONTENT_TYPE_VALUE_JSON))
              .withHeader(traceIdHeader)
              .withBody(readJSON("mocks/auth-adapter-v1/get-roles.400.json"))
              .withDelay(TimeUnit.MILLISECONDS, randomDelay);
        });
  }

  private static Header buildTraceId() {
    return header("trace-id", UUID.randomUUID().toString().replaceAll("-", "").toLowerCase());
  }

  private static final double EXPONENTIAL_SCALE = 250;

  private static long buildDelayWithProbabilityDensity() {
    double u = ThreadLocalRandom.current().nextDouble();
    long delay = (long) (-EXPONENTIAL_SCALE * Math.log(1 - u));
    delay = Math.max(250, Math.min(2000, delay));
    return delay;
  }
}
