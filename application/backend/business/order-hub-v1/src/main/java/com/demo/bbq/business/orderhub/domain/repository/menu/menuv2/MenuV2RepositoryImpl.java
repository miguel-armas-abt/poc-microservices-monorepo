package com.demo.bbq.business.orderhub.domain.repository.menu.menuv2;

import com.demo.bbq.business.orderhub.domain.exception.OrderHubException;
import com.demo.bbq.business.orderhub.domain.repository.menu.MenuRepository;
import com.demo.bbq.business.orderhub.domain.repository.menu.wrapper.response.MenuOptionResponseWrapper;
import com.demo.bbq.business.orderhub.domain.repository.menu.wrapper.request.MenuOptionSaveRequestWrapper;
import com.demo.bbq.business.orderhub.domain.repository.menu.wrapper.request.MenuOptionUpdateRequestWrapper;
import com.demo.bbq.support.httpclient.retrofit.reactive.HttpStreamingTransformer;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MenuV2RepositoryImpl implements MenuRepository {

  private final MenuV2Repository menuV2Repository;

  @Override
  public Observable<MenuOptionResponseWrapper> findByCategory(String category) {
    return menuV2Repository.findByCategory(category).compose(HttpStreamingTransformer.of(MenuOptionResponseWrapper.class));
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
            .map(error -> Completable.error(OrderHubException.ERROR0000.buildException()))
            .orElse(Completable.complete()));
  }

  @Override
  public boolean supports(Class<?> selectedClass) {
    return this.getClass().isAssignableFrom(selectedClass);
  }
}
