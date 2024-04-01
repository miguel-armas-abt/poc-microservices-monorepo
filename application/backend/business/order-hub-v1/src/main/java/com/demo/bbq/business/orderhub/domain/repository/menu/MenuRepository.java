package com.demo.bbq.business.orderhub.domain.repository.menu;

import com.demo.bbq.business.orderhub.application.helper.serviceselector.SelectedServiceBase;
import com.demo.bbq.business.orderhub.domain.repository.menu.wrapper.response.MenuOptionResponseWrapper;
import com.demo.bbq.business.orderhub.domain.repository.menu.wrapper.request.MenuOptionSaveRequestWrapper;
import com.demo.bbq.business.orderhub.domain.repository.menu.wrapper.request.MenuOptionUpdateRequestWrapper;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;

public interface MenuRepository extends SelectedServiceBase {

  Observable<MenuOptionResponseWrapper> findByCategory(String category);

  Maybe<MenuOptionResponseWrapper> findByProductCode(String productCode);

  Completable save(MenuOptionSaveRequestWrapper menuOption);

  Completable update(String productCode, MenuOptionUpdateRequestWrapper menuOption);

  Completable delete(String productCode);

}
