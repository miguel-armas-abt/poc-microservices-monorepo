package com.demo.bbq.properties;

import com.demo.bbq.properties.configuration.Configuration;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationProperties {

  private Configuration configuration;
}
