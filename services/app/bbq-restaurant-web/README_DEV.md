ng g service  infrastructure/repository/restClients/baseRestClient --skip-tests=true
ng g service  infrastructure/repository/restClients/menu/connector/menuApiConnector --skip-tests=true
ng g class infrastructure/repository/restClients/menu/dto/menuDto --skip-tests=true
ng g class infrastructure/repository/restClients/menu/dto/menuSaveRequest --skip-tests=true
ng g class infrastructure/repository/restClients/menu/dto/menuUpdateRequest --skip-tests=true

ng g class application/menu/domain/models/menu --skip-tests=true
ng g service application/menu/useCases/menuHandler --skip-tests=true

ng g c features/menuList/components/menuList --skip-tests=true
ng g service features/menuList/services/menuList --skip-tests=true


Include Angular Material
ng add @angular/material

