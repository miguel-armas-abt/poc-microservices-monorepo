package com.demo.bbq.commons.aspect;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MonitorService {

  @Async
  public void handleLogs() {}
}
