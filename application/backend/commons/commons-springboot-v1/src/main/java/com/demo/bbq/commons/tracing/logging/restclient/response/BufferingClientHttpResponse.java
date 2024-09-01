package com.demo.bbq.commons.tracing.logging.restclient.response;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;

public class BufferingClientHttpResponse implements ClientHttpResponse {

  private final ClientHttpResponse response;
  private byte[] body;

  public BufferingClientHttpResponse(ClientHttpResponse response) throws IOException {
    this.response = response;
    try (InputStream responseBodyStream = response.getBody()) {
      this.body = StreamUtils.copyToByteArray(responseBodyStream);
    }
  }

  @Override
  public HttpStatusCode getStatusCode() throws IOException {
    return this.response.getStatusCode();
  }

  @Override
  public String getStatusText() throws IOException {
    return this.response.getStatusText();
  }

  @Override
  public void close() {
    this.response.close();
  }

  @Override
  public InputStream getBody() throws IOException {
    return new ByteArrayInputStream(this.body);
  }

  @Override
  public HttpHeaders getHeaders() {
    return this.response.getHeaders();
  }
}
