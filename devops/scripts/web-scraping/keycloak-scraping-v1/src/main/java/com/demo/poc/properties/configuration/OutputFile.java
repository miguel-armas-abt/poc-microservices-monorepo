package com.demo.poc.properties.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OutputFile {

  private String absolutePath;
  private String projectPath;
  private String name;

  @Override
  public String toString() {
    return this.absolutePath + this.projectPath + this.name;
  }
}
