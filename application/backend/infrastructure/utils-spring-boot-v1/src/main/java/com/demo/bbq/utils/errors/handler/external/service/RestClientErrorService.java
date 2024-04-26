package com.demo.bbq.utils.errors.handler.external.service;

import com.demo.bbq.utils.errors.handler.external.ExternalClientErrorService;
import com.demo.bbq.utils.errors.handler.external.ExternalClientErrorWrapper;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.web.client.HttpStatusCodeException;

public interface RestClientErrorService<T extends ExternalClientErrorWrapper> extends ExternalClientErrorService<T> {

  Pair<String, String> getCodeAndMessage(HttpStatusCodeException httpException);

}