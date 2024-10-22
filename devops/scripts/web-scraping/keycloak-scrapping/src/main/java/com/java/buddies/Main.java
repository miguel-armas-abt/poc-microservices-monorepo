package com.java.buddies;

import com.java.buddies.spiders.*;

public class Main {
  public static void main(String[] args) throws InterruptedException {
    LoginSpider.login();
    RealmCreatorSpider.createRealm();
    RealmConfigSpider.configRealm();
    UserSpider.configUser();
    RoleSpider.configRole();
    UserRoleSpider.configRoleMappings();
    ClientSpider.configClient();
  }
}