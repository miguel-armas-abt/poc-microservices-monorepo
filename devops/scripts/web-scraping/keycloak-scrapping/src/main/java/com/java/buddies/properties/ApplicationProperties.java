package com.java.buddies.properties;

import com.java.buddies.properties.configuration.Configuration;
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
