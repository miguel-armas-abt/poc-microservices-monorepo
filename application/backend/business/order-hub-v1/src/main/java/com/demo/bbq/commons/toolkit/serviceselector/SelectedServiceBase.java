package com.demo.bbq.commons.toolkit.serviceselector;

public interface SelectedServiceBase {

  boolean supports(Class<?> selectedClass);
}