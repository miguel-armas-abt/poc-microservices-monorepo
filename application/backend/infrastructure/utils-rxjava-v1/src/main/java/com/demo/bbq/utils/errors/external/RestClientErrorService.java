package com.demo.bbq.utils.errors.external;

import java.util.Optional;
import okhttp3.ResponseBody;
import org.apache.commons.lang3.tuple.Pair;

public interface RestClientErrorService<T extends ExternalClientErrorWrapper> extends ExternalClientErrorService<T> {

  Optional<Pair<String, String>> getCodeAndMessage(ResponseBody errorBody);

}
