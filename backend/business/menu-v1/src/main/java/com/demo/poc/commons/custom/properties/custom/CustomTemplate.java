package com.demo.poc.commons.custom.properties.custom;

import com.demo.poc.commons.custom.properties.custom.cache.CacheTemplate;
import com.demo.poc.commons.custom.properties.custom.functional.FunctionalTemplate;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class CustomTemplate {

  private Map<String, CacheTemplate> cache;

  private FunctionalTemplate functional;
}
