package com.demo.bbq.business.orderhub.application.helper.serviceselector;

public interface SelectedServiceBase {

  boolean supports(Class<?> selectedClass);
}