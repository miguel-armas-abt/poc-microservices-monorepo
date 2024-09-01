package com.demo.bbq.commons.properties.dto.cache;

import com.demo.bbq.commons.cache.enums.CleaningFrequency;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CacheTemplate implements Serializable {

  private CleaningFrequency cleaningFrequency;
  private long customTtl;
}