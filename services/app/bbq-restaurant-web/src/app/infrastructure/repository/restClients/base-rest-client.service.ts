import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export abstract class BaseRestClientService {

  protected baseURI: string;

  protected headers: HttpHeaders = new HttpHeaders({
    'Content-Type': 'application/json'
  });

  protected constructor(protected http: HttpClient) {
    this.baseURI = '';
  }

}
