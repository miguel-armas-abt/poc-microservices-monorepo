import {Component, OnInit} from '@angular/core';
import {MenuDto} from "../../../../infrastructure/repository/restClients/menu/dto/menu-dto";
import {MenuHandlerService} from "../../../../application/menu/services/menu-handler.service";
import {MatCardModule} from "@angular/material/card";
import {MatButtonModule} from '@angular/material/button';
import {CommonModule} from "@angular/common";

@Component({
  selector: 'app-menu-list',
  standalone: true,
  imports: [
    MatCardModule,
    MatButtonModule,
    CommonModule
  ],
  templateUrl: './menu-list.component.html',
  styleUrl: './menu-list.component.css'
})
export class MenuListComponent implements OnInit {

  menuOptions: MenuDto[] = [];

  constructor(private menuHandlerService: MenuHandlerService) { }

  ngOnInit(): void {
    this.menuHandlerService.findAll()
      .subscribe(menuOptionsRecovered => this.menuOptions = menuOptionsRecovered)

    console.log(this.menuOptions);
  }

}
