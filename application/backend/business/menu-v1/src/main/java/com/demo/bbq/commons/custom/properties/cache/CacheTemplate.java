package com.demo.bbq.commons.custom.properties.cache;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class CacheTemplate implements Serializable {

  private CleaningFrequency cleaningFrequency;
  private long customTtl;
}