package com.demo.bbq.repository.menu;

import com.demo.bbq.application.helper.serviceselector.SelectedServiceBase;
import com.demo.bbq.repository.menu.wrapper.response.MenuOptionResponseWrapper;
import com.demo.bbq.repository.menu.wrapper.request.MenuOptionSaveRequestWrapper;
import com.demo.bbq.repository.menu.wrapper.request.MenuOptionUpdateRequestWrapper;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import jakarta.servlet.http.HttpServletRequest;

public interface MenuRepositoryHelper extends SelectedServiceBase {

  Observable<MenuOptionResponseWrapper> findByCategory(HttpServletRequest httpRequest,
                                                       String category);

  Maybe<MenuOptionResponseWrapper> findByProductCode(HttpServletRequest httpRequest,
                                                     String productCode);

  Completable save(HttpServletRequest httpRequest,
                   MenuOptionSaveRequestWrapper menuOption);

  Completable update(HttpServletRequest httpRequest,
                     String productCode,
                     MenuOptionUpdateRequestWrapper menuOption);

  Completable delete(HttpServletRequest httpRequest,
                     String productCode);

}
