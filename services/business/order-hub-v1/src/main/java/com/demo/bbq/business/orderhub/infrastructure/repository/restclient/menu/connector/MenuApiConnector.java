package com.demo.bbq.business.orderhub.infrastructure.repository.restclient.menu.connector;

import com.demo.bbq.business.orderhub.infrastructure.repository.restclient.menu.dto.MenuOptionDto;
import com.demo.bbq.business.orderhub.infrastructure.repository.restclient.menu.dto.MenuOptionSaveRequestDto;
import com.demo.bbq.business.orderhub.infrastructure.repository.restclient.menu.dto.MenuOptionUpdateRequestDto;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;

public interface MenuApiConnector {

  Observable<MenuOptionDto> findByCategory(String category);

  Maybe<MenuOptionDto> findByProductCode(String productCode);

  Completable save(MenuOptionSaveRequestDto menuOption);

  Completable update(String productCode, MenuOptionUpdateRequestDto menuOption);

  Completable delete(String productCode);

  boolean supports(Class<?> selectedClass);
}
