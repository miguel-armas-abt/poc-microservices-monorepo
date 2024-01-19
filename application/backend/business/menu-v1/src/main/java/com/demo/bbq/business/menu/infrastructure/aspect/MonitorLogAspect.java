package com.demo.bbq.business.menu.infrastructure.aspect;

import java.util.function.BiFunction;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class MonitorLogAspect {

  private final MonitorService monitorService;

  @AfterReturning(value = "@annotation(monitorLog)", returning = "response")
  public void handleResponse(JoinPoint joinPoint, MonitorLog monitorLog, Object response) {
    log.info("Response:" + new Gson().toJson(response));
    log.info("Arguments:" + new Gson().toJson(getArgument.apply(joinPoint,0)));
    monitorService.handleLogs();
    // process result
  }

  @Before(value = "@annotation(monitorLog)")
  public void handleBefore(JoinPoint joinPoint, MonitorLog monitorLog) {
    log.info("Arguments:" + new Gson().toJson(getArgument.apply(joinPoint,0)));
    monitorService.handleLogs();
    // process arguments
  }

  @AfterThrowing(value = "@annotation(monitorLog)", throwing = "throwable")
  public void handleException(JoinPoint joinPoint, MonitorLog monitorLog, Throwable throwable) {
    log.error("Error: " + throwable.getMessage());
    log.info("Arguments:" + new Gson().toJson(getArgument.apply(joinPoint,0)));
    monitorService.handleLogs();
    // process exception
  }

  @Around(value = "@annotation(monitorLog)")
  public Object handleAround(ProceedingJoinPoint joinPoint, MonitorLog monitorLog) throws Throwable {
    log.info("Arguments:" + new Gson().toJson(getArgument.apply(joinPoint,0)));
    monitorService.handleLogs();
    return joinPoint.proceed();
  }

  private final BiFunction<JoinPoint, Integer, Object> getArgument = (joinPoint, argumentIndex) -> joinPoint.getArgs()[argumentIndex];
}
