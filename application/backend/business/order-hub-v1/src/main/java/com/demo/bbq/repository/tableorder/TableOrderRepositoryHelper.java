package com.demo.bbq.repository.tableorder;

import static com.demo.bbq.repository.tableorder.TableOrderConfig.TABLE_PLACEMENT_SERVICE_NAME;

import com.demo.bbq.application.dto.tableorder.request.MenuOrderRequestDTO;
import com.demo.bbq.config.properties.ServiceConfigurationProperties;
import com.demo.bbq.repository.tableorder.wrapper.TableOrderRequestWrapper;
import io.reactivex.rxjava3.core.Single;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import okhttp3.ResponseBody;
import org.springframework.stereotype.Component;
import retrofit2.Response;

@RequiredArgsConstructor
@Component
public class TableOrderRepositoryHelper {

  private final ServiceConfigurationProperties properties;
  private final TableOrderRepository repository;

  public Single<ResponseBody> generateTableOrder(HttpServletRequest httpRequest,
                                          List<MenuOrderRequestDTO> requestedMenuOrderList,
                                          Integer tableNumber) {
    return repository.generateTableOrder(getHeaders(httpRequest), requestedMenuOrderList, tableNumber);
  }

  public Single<TableOrderRequestWrapper> findByTableNumber(HttpServletRequest httpRequest, Integer tableNumber) {
    return repository.findByTableNumber(getHeaders(httpRequest), tableNumber);
  }

  public Single<Response<Void>> cleanTable(HttpServletRequest httpRequest, Integer tableNumber) {
    return repository.cleanTable(getHeaders(httpRequest), tableNumber);
  }

  private Map<String, String> getHeaders(HttpServletRequest httpRequest) {
    return properties.searchHeaders(httpRequest, TABLE_PLACEMENT_SERVICE_NAME);
  }
}
