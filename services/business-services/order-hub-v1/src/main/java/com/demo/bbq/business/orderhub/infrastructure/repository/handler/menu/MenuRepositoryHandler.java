package com.demo.bbq.business.orderhub.infrastructure.repository.handler.menu;

import com.demo.bbq.business.orderhub.infrastructure.repository.cache.menu.MenuCache;
import com.demo.bbq.business.orderhub.infrastructure.repository.restclient.menu.MenuApi;
import com.demo.bbq.business.orderhub.infrastructure.repository.restclient.menu.dto.MenuOptionDto;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.adapter.rxjava.RxJava2Adapter;

@Component
@RequiredArgsConstructor
public class MenuRepositoryHandler {

  private final MenuApi menuApi;
  private final MenuCache menuCache;

  public Observable<MenuOptionDto> findAll() {
    return menuCache.findAll()
        .switchIfEmpty(RxJava2Adapter.fluxToObservable(menuApi.findByCategory(null))
            .flatMapCompletable(menuCache::save)
            .andThen(Observable.defer(menuCache::findAll)));
  }

  public Observable<MenuOptionDto> findByCategory(String menuCategory) {
    return findAll().filter(menuOptionDto -> menuCategory.equals(menuOptionDto.getCategory()));
  }

  public Maybe<MenuOptionDto> findById(Long menuOptionId) {
    return findAll().filter(menuOptionDto -> menuOptionId.equals(menuOptionDto.getId()))
        .firstElement();
  }

}
