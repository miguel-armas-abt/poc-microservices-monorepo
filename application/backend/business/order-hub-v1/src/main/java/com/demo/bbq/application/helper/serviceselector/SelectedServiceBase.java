package com.demo.bbq.application.helper.serviceselector;

public interface SelectedServiceBase {

  boolean supports(Class<?> selectedClass);
}