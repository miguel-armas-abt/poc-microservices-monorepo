package com.demo.bbq.business.diningroomorder.infrastructure.repository.restclient;

import com.demo.bbq.business.diningroomorder.infrastructure.repository.restclient.menuoptionv1.MenuOptionV1RetrofitApi;
import com.demo.bbq.business.diningroomorder.infrastructure.repository.restclient.menuoptionv1.MenuOptionV1WebClientApi;
import com.demo.bbq.business.diningroomorder.infrastructure.repository.restclient.menuoptionv2.MenuOptionV2RetrofitApi;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Getter
public class MenuOptionStrategyInjector {

  private final MenuOptionV1RetrofitApi menuOptionV1RetrofitApi;
  private final MenuOptionV1WebClientApi menuOptionV1WebClientApi;
  private final MenuOptionV2RetrofitApi menuOptionV2RetrofitApi;
}
