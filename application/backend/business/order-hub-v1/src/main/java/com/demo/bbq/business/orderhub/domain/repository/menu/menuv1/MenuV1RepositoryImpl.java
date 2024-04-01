package com.demo.bbq.business.orderhub.domain.repository.menu.menuv1;

import com.demo.bbq.business.orderhub.domain.exception.OrderHubException;
import com.demo.bbq.business.orderhub.domain.repository.menu.MenuRepository;
import com.demo.bbq.business.orderhub.domain.repository.menu.wrapper.request.MenuOptionSaveRequestWrapper;
import com.demo.bbq.business.orderhub.domain.repository.menu.wrapper.request.MenuOptionUpdateRequestWrapper;
import com.demo.bbq.business.orderhub.domain.repository.menu.wrapper.response.MenuOptionResponseWrapper;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MenuV1RepositoryImpl implements MenuRepository {

  private final MenuV1Repository menuV1Repository;

  @Override
  public Observable<MenuOptionResponseWrapper> findByCategory(String category) {
    return menuV1Repository.findByCategory(category).flatMapObservable(Observable::fromIterable);
  }

  @Override
  public Maybe<MenuOptionResponseWrapper> findByProductCode(String productCode) {
    return menuV1Repository.findByProductCode(productCode).toMaybe();
  }

  @Override
  public Completable save(MenuOptionSaveRequestWrapper menuOption) {
    return menuV1Repository.save(menuOption).ignoreElement();
  }

  @Override
  public Completable update(String productCode, MenuOptionUpdateRequestWrapper menuOption) {
    return menuV1Repository.update(productCode, menuOption).ignoreElement();
  }

  @Override
  public Completable delete(String productCode) {
    return menuV1Repository
        .delete(productCode)
        .ignoreElement()
        .onErrorResumeNext(throwable -> Optional.ofNullable(((Exception) throwable).getMessage())
            .filter(error -> error.equals("HTTP 400 Bad Request"))
            .map(error -> Completable.error(OrderHubException.ERROR0000.buildException()))
            .orElse(Completable.complete()));
  }

  @Override
  public boolean supports(Class<?> selectedClass) {
    return this.getClass().isAssignableFrom(selectedClass);
  }
}
