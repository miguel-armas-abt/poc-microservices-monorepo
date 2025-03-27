package com.demo.poc.commons.core.properties.obfuscation;

import java.util.Optional;
import java.util.Set;

public interface ObfuscationTemplate {

  Optional<Set<String>> bodyFields();

  Optional<Set<String>> headers();
}