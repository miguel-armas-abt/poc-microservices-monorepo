package com.demo.bbq.utils.errors.handler.external;

public interface ExternalClientErrorService<T extends ExternalClientErrorWrapper> {

  boolean supports(Class<?> wrapperClass);
}
