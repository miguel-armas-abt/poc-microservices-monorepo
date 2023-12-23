import {Injectable} from '@angular/core';
import {Observable} from "rxjs";
import {MenuDto} from "../../../infrastructure/repository/restClients/menu/dto/menu-dto";
import {
  MenuApiConnectorService
} from "../../../infrastructure/repository/restClients/menu/connector/menu-api-connector.service";

@Injectable({
  providedIn: 'root'
})
export class MenuHandlerService {

  constructor(private menuApiConnectorService: MenuApiConnectorService) {}

  public findAll():Observable<MenuDto[]> {
    return this.menuApiConnectorService.findAll();
  }

  public findByProductCode(productCode: string): Observable<MenuDto> {
    return this.menuApiConnectorService.findByProductCode(productCode);
  }

}
