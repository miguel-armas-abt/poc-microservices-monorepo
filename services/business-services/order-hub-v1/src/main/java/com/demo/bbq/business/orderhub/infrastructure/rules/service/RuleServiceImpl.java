package com.demo.bbq.business.orderhub.infrastructure.rules.service;

import com.demo.bbq.business.orderhub.infrastructure.rules.properties.RuleInitMap;
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
public class RuleServiceImpl implements RuleService {

  private final RuleInitMap ruleInitMap;

  private static final String RULES_DIRECTORY = "rules/";

  @Override
  public <T> T process(T ruleFilter) {
    String className = ruleFilter.getClass().getSimpleName();

    String ruleFile = ruleInitMap.getRuleInfoMap().get(className).getRuleFile();
    KieContainer kieContainer = createKieContainer(ruleFile);

    StatelessKieSession statelessKieSession = kieContainer.newStatelessKieSession();
    statelessKieSession.execute(ruleFilter);

    return ruleFilter;
  }

  private static KieContainer createKieContainer(String fileName) {
    KieServices kieServices = KieServices.Factory.get();
    KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
    kieFileSystem.write(ResourceFactory.newClassPathResource(RULES_DIRECTORY + fileName));
    KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem).buildAll();

    if (kieBuilder.getResults().hasMessages(org.kie.api.builder.Message.Level.ERROR)) {
      throw new RuntimeException("Rules compilation error: " + kieBuilder.getResults());
    }

    KieRepository kieRepository = kieServices.getRepository();
    return kieServices.newKieContainer(kieRepository.getDefaultReleaseId());
  }

}
