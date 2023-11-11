package com.demo.bbq.business.orderhub.infrastructure.resource.rest.menu;

import com.demo.bbq.business.orderhub.infrastructure.repository.restclient.menu.MenuApi;
import com.demo.bbq.business.orderhub.infrastructure.repository.restclient.menu.dto.MenuOptionDto;
import com.demo.bbq.business.orderhub.infrastructure.resource.rest.OrderHubRestService;
import io.reactivex.Observable;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.adapter.rxjava.RxJava2Adapter;

@Component
@RequiredArgsConstructor
public class MenuRestServiceImpl extends OrderHubRestService implements MenuRestService{

  private final MenuApi menuApi;

  @GetMapping(value = "/menu-options", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
  public Observable<MenuOptionDto> findMenuByCategory(HttpServletRequest servletRequest,
                                                      @RequestParam(value = "category") String categoryCode) {
    return RxJava2Adapter.fluxToObservable(menuApi.findByCategory(categoryCode));
  }
}
