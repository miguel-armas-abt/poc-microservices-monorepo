package com.demo.bbq.service;

import com.demo.bbq.spiders.*;
import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class ScrapingService {

  private final ClientSpider clientSpider;
  private final LoginSpider loginSpider;
  private final RealmConfigSpider realmConfigSpider;
  private final RealmCreatorSpider realmCreatorSpider;
  private final RoleSpider roleSpider;
  private final UserRoleSpider userRoleSpider;
  private final UserSpider userSpider;

  public void scrap() throws InterruptedException {
    loginSpider.login();
    realmCreatorSpider.createRealm();
    realmConfigSpider.configRealm();
    userSpider.configUser();
    roleSpider.configRole();
    userRoleSpider.configRoleMappings();
    clientSpider.configClient();
  }
}
