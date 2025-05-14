import { Routes } from '@angular/router';

export const routes: Routes = [
  {path: 'menuList', loadComponent: () => import('./features/menuList/components/menu-list/menu-list.component')
      .then(component => component.MenuListComponent)
  }
];
