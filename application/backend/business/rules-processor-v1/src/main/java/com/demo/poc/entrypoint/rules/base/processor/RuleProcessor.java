package com.demo.poc.entrypoint.rules.base.processor;

import com.demo.poc.commons.custom.exceptions.RulesCompilationException;
import com.demo.poc.commons.custom.properties.ApplicationProperties;
import com.demo.poc.entrypoint.rules.base.rule.Rule;
import lombok.RequiredArgsConstructor;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieRepository;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.StatelessKieSession;
import org.kie.internal.io.ResourceFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RuleProcessor {

  private final ApplicationProperties properties;

  public <R extends Rule> R process(R rule) {
    String className = rule.getClass().getSimpleName();

    String ruleFile = properties.getRules().getStrategies().get(className);
    KieContainer kieContainer = createKieContainer(ruleFile);

    StatelessKieSession statelessKieSession = kieContainer.newStatelessKieSession();
    statelessKieSession.execute(rule);

    return rule;
  }

  private KieContainer createKieContainer(String fileName) {
    KieServices kieServices = KieServices.Factory.get();
    KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
    kieFileSystem.write(ResourceFactory.newClassPathResource(properties.getRules().getDirectory() + fileName));
    KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem).buildAll();

    if (kieBuilder.getResults().hasMessages(org.kie.api.builder.Message.Level.ERROR)) {
      throw new RulesCompilationException(kieBuilder.getResults().toString());
    }

    KieRepository kieRepository = kieServices.getRepository();
    return kieServices.newKieContainer(kieRepository.getDefaultReleaseId());
  }

}