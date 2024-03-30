package com.demo.bbq.business.orderhub.domain.repository.menu;

import com.demo.bbq.business.orderhub.application.helper.serviceselector.SelectedServiceBase;
import com.demo.bbq.business.orderhub.domain.repository.menu.wrapper.response.MenuOptionResponseWrapper;
import com.demo.bbq.business.orderhub.domain.repository.menu.wrapper.request.MenuOptionSaveRequestWrapper;
import com.demo.bbq.business.orderhub.domain.repository.menu.wrapper.request.MenuOptionUpdateRequestWrapper;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;

public interface MenuRepository extends SelectedServiceBase {

  Observable<MenuOptionResponseWrapper> findByCategory(String category);

  Maybe<MenuOptionResponseWrapper> findByProductCode(String productCode);

  Completable save(MenuOptionSaveRequestWrapper menuOption);

  Completable update(String productCode, MenuOptionUpdateRequestWrapper menuOption);

  Completable delete(String productCode);

}
