package com.demo.bbq.repository.menu.menuv2;

import com.demo.bbq.repository.menu.MenuRepository;
import com.demo.bbq.repository.menu.wrapper.response.MenuOptionResponseWrapper;
import com.demo.bbq.repository.menu.wrapper.request.MenuOptionSaveRequestWrapper;
import com.demo.bbq.repository.menu.wrapper.request.MenuOptionUpdateRequestWrapper;
import com.demo.bbq.utils.errors.exceptions.BusinessException;
import com.demo.bbq.utils.restclient.retrofit.ReactiveTransformer;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MenuV2RepositoryImpl implements MenuRepository {

  private final MenuV2Repository menuV2Repository;

  @Override
  public Observable<MenuOptionResponseWrapper> findByCategory(String category) {
    return menuV2Repository.findByCategory(category)
        .compose(ReactiveTransformer.of(MenuOptionResponseWrapper.class));
  }

  @Override
  public Maybe<MenuOptionResponseWrapper> findByProductCode(String productCode) {
    return menuV2Repository.findByProductCode(productCode).toMaybe();
  }

  @Override
  public Completable save(MenuOptionSaveRequestWrapper menuOption) {
    return menuV2Repository.save(menuOption).ignoreElement();
  }

  @Override
  public Completable update(String productCode, MenuOptionUpdateRequestWrapper menuOption) {
    return menuV2Repository.update(productCode, menuOption).ignoreElement();
  }

  @Override
  public Completable delete(String productCode) {
    return menuV2Repository
        .delete(productCode)
        .ignoreElement()
        .onErrorResumeNext(throwable -> Optional.ofNullable(((Exception) throwable).getMessage())
            .filter(error -> error.equals("HTTP 400 Bad Request"))
            .map(error -> Completable.error(new BusinessException("MenuOrderNotExists")))
            .orElse(Completable.complete()));
  }

  @Override
  public boolean supports(Class<?> selectedClass) {
    return this.getClass().isAssignableFrom(selectedClass);
  }
}
