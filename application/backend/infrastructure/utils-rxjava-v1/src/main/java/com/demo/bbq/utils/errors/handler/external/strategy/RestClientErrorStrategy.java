package com.demo.bbq.utils.errors.handler.external.strategy;

import java.util.Optional;
import okhttp3.ResponseBody;
import org.apache.commons.lang3.tuple.Pair;

public interface RestClientErrorStrategy extends ExternalErrorStrategy {

  Optional<Pair<String, String>> getCodeAndMessage(ResponseBody errorBody);

}
