package com.demo.bbq.utils.errors.external;

public interface ExternalClientErrorService<T extends ExternalClientErrorWrapper> {

  boolean supports(Class<?> wrapperClass);
}
