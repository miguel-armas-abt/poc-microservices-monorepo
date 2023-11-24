package com.demo.bbq.business.orderhub.infrastructure.repository.cache.menu;

import com.demo.bbq.business.orderhub.infrastructure.repository.cache.helper.CacheHelper;
import com.demo.bbq.business.orderhub.infrastructure.repository.restclient.menu.dto.MenuOptionDto;
import io.reactivex.Completable;
import io.reactivex.Observable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class MenuCache {

  private final CacheHelper<MenuOptionDto> menuCacheHelper;

  private final String CACHE_NAME = "menu.option";

  public Completable save(MenuOptionDto menuOption) {
    return menuCacheHelper.put(CACHE_NAME, String.valueOf(menuOption.getProductCode()), menuOption);
  }

  public Observable<MenuOptionDto> findAll() {
    return menuCacheHelper.getAll(CACHE_NAME, MenuOptionDto.class);
  }

}
