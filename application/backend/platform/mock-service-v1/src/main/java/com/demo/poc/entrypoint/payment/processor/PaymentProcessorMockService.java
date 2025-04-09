package com.demo.poc.entrypoint.payment.processor;

import static com.demo.poc.commons.utils.DelayGenerator.generateRandomDelay;
import static com.demo.poc.commons.utils.HeadersGenerator.contentType;
import static com.demo.poc.commons.utils.HeadersGenerator.generateTraceId;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

import com.demo.poc.commons.config.MockService;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.Header;
import org.mockserver.model.HttpStatusCode;
import org.springframework.stereotype.Component;

@Component
public class PaymentProcessorMockService implements MockService {

  @Override
  public void loadMocks(ClientAndServer mockServer) throws IOException {
    mockServer
        .when(request()
            .withMethod("POST")
            .withPath("/external/payments/v1/execute"))
        .respond(request -> {

          long randomDelay = generateRandomDelay();
          Header traceIdHeader = generateTraceId();

          return response()
              .withStatusCode(HttpStatusCode.OK_200.code())
              .withHeader(contentType("application/x-ndjson"))
              .withHeader(traceIdHeader)
              .withBody("{\"id\":\"1234\",\"status\":\"Success\"}\n{\"id\":\"2345\",\"status\":\"Success\"}\n{\"id\":\"3456\",\"status\":\"Failed\"}")
              .withDelay(TimeUnit.MILLISECONDS, randomDelay);
        });
  }
}
