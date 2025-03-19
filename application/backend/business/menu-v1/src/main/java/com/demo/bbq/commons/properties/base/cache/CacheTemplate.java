package com.demo.bbq.commons.properties.base.cache;

import com.demo.bbq.commons.cache.enums.CleaningFrequency;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class CacheTemplate implements Serializable {

  private CleaningFrequency cleaningFrequency;
  private long customTtl;
}