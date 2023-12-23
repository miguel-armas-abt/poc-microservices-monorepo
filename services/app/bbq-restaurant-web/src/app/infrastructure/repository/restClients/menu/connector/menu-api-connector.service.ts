import { Injectable } from '@angular/core';
import {BaseRestClientService} from "../../base-rest-client.service";
import {MenuDto} from "../dto/menu-dto";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {MenuSaveRequest} from "../dto/menu-save-request";
import {MenuUpdateRequest} from "../dto/menu-update-request";

@Injectable({
  providedIn: 'root'
})
export class MenuApiConnectorService extends BaseRestClientService {

  override baseURI = '/bbq/business/menu/v1/menu-options';
  constructor(http: HttpClient) {
    super(http);
  }

  public findAll(): Observable<MenuDto[]> {
    return this.http.get<MenuDto[]>(this.baseURI);
  }

  public findByProductCode(productCode: string): Observable<MenuDto> {
    return this.http.get<MenuDto>(`${this.baseURI}/${productCode}`);
  }

  public deleteByProductCode(productCode: string): Observable<void>{
    return this.http.delete<void>(`${this.baseURI}/${productCode}`);
  }

  public save(menuSaveRequest: MenuSaveRequest): Observable<void> {
    return this.http.post<void>(
      this.baseURI,
      menuSaveRequest,
      {headers: this.headers}
    );
  }

  public update(menuUpdateRequest: MenuUpdateRequest, productCode: string): Observable<void> {
    return this.http.put<void>(
      `${this.baseURI}/${productCode}`,
      menuUpdateRequest,
      {headers: this.headers}
    );
  }

}
