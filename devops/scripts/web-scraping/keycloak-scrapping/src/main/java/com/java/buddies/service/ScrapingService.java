package com.java.buddies.service;

import com.google.inject.Inject;
import com.java.buddies.spiders.*;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class ScrapingService {

  @Inject
  private final ClientSpider clientSpider;

  @Inject
  private final LoginSpider loginSpider;

  @Inject
  private final RealmConfigSpider realmConfigSpider;

  @Inject
  private final RealmCreatorSpider realmCreatorSpider;

  @Inject
  private final RoleSpider roleSpider;

  @Inject
  private final UserRoleSpider userRoleSpider;

  @Inject
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
