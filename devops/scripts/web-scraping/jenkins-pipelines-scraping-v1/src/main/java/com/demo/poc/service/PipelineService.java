package com.demo.poc.service;

import com.demo.poc.spiders.*;
import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class PipelineService {

  private final LoginSpider loginSpider;
  private final PipelineSpider pipelineSpider;

  public void scrapSettings() throws InterruptedException {
    loginSpider.loginIfNotPresent();
    pipelineSpider.configureNewPipeline();
  }
}
