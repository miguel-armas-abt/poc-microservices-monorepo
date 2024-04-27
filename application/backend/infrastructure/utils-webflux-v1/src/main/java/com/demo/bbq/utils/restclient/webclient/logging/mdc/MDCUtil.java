package com.demo.bbq.utils.restclient.webclient.logging.mdc;

import static com.demo.bbq.utils.restclient.webclient.logging.constants.LoggingConstant.MDC_NAME_COMPONENT;
import static com.demo.bbq.utils.restclient.webclient.logging.constants.LoggingConstant.HEADER_TRACE;
import static com.demo.bbq.utils.restclient.webclient.logging.constants.LoggingConstant.MDC_TRACE_ID;
import static com.demo.bbq.utils.restclient.webclient.logging.constants.LoggingConstant.HEADER_PARENT;
import static com.demo.bbq.utils.restclient.webclient.logging.constants.LoggingConstant.MDC_PARENT_ID;
import static com.demo.bbq.utils.restclient.webclient.logging.constants.LoggingConstant.HEADER_TRACEPARENT;
import static com.demo.bbq.utils.restclient.webclient.logging.constants.LoggingConstant.MDC_TRACEPARENT;

import com.demo.bbq.utils.restclient.webclient.properties.LoggingBaseProperties;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.http.HttpHeaders;

public class MDCUtil {

  public static void putMDCTracking(LoggingBaseProperties loggingProperties, HttpHeaders httpHeaders) {

    ThreadContext.put(MDC_NAME_COMPONENT, loggingProperties.getProjectName());
    putHeaderInMDC(httpHeaders, HEADER_TRACE, MDC_TRACE_ID);
    putHeaderInMDC(httpHeaders, HEADER_PARENT, MDC_PARENT_ID);
    putHeaderInMDC(httpHeaders, HEADER_TRACEPARENT, MDC_TRACEPARENT);
  }

  private static void putHeaderInMDC(HttpHeaders httpHeaders, String headerName, String mdcKey) {
    String headerValue = httpHeaders.getFirst(headerName);
    ThreadContext.put(mdcKey, headerValue != null ? headerValue : "");
  }

}
