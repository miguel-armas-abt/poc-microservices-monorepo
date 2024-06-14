package com.demo.bbq.commons.restclient.webclient.dto;

import java.time.Duration;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class Pool {

  private int size = 20;
  private int queue = 40;
  private Duration queueTimeout = Duration.ofSeconds(5L);
  private String name = "default-pool";
}
