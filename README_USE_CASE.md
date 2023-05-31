# Caso de estudio: Reactive BBQ Restaurant

> Reactive BBQ es un restaurante que se enfoca en sabores tradicionales de barbacoa. Comenzó como una tienda familiar, 
> luego creció progresivamente abriendo locales en diferentes ciudades del país y actualmente se ha globalizado.
>
> El software creció orgánicamente con el negocio, se atornillaron cosas en diferentes lugares hasta el punto en que 
> ahora se tiene una aplicación gigante que hace de todo. Por ejemplo, permite gestionar los inventarios, los precios 
> del menú, las reservas online, los pedidos en el comedor, el delivery, etc. Todas estas funcionalidades se han 
> agrupado en un solo sistema que está luchando por su propio peso. Tiene muchos procesos heredados que requieren que el
> sistema se apague durante un período de tiempo, lo que significa que los restaurantes no pueden continuar con sus 
> actividades, por lo que deben tratar de orientar ese tiempo de inactividad a períodos en los que no hay mucha 
> actividad en sus restaurantes o cuando los restaurantes están cerrados. Eso estaba bien cuando todas sus ubicaciones 
> estaban en América del Norte, pero a medida que se han globalizado, cada vez es más difícil encontrar esos períodos de
> tiempo en los que pueden tener tiempo de inactividad.
>
> En consecuencia Reactive BBQ Restaurant experimentará una importante actualización de su software, ya que busca 
> soportar su aplicación sobre una arquitectura de microservicios.

- [1. Expertos en el dominio](#1-expertos-en-el-dominio)
- [2. Bounded contexts](#2-bounded-contexts)
- [3. Web Services](#3-web-services)

# 1. Expertos en el dominio
Tras hablar con los expertos en el dominio "restaurante" y entender su vocabulario para usarlo en nuestro modelo pudimos
identificar las siguientes actividades utilizando la notación `sujeto-verbo-objeto`

- Anfitrión
    - El anfitrión verifica las reservas actuales.
    - El anfitrión crea una reserva para un cliente.
    - El anfitrión asienta al cliente con reserva.

- Mesero
    - El mesero toma el pedido en el comedor.
    - El mesero entrega el pedido en el comedor.
    - El mesero cobra el pago de un pedido en el comedor.
  
- Chef de cocina
    - El chef de cocina prepara un pedido.
    - El chef de cocina notifica al mesero que el pedido está completo.
    - El chef de cocina inspecciona los pedidos.

- Conductor de delivery
    - El conductor recoge un pedido en el restaurante.
    - El conductor entrega un pedido al cliente.
    - El conductor cobra el pago de un pedido.

- Cliente en línea
    - El cliente en línea agrega elementos del menú a un pedido.
    - El cliente en línea realiza el pago de un pedido.
    - El cliente en línea hace una reserva.

# 2. Bounded contexts
De acuerdo a los objetos definidos en las actividades anteriores se identificaron los siguientes bounded contexts y 
algunas palabras de sus lenguajes ubicuos

- `Menu:` option, drink, main dish 
- `Order`: dining room, kitchen, notification, delivery, tip
- `Payment`: invoice, credit, debit, cash
- `Reservation`: table, reservation, customer, time, location

# 3. Web Services

## 3.1. Business API Menu Option V1
Gesiona las opciones de menú que ofrece el restaurante.

| Endpoint                                            | Método | Descripción                                                                                       |
|-----------------------------------------------------|--------|---------------------------------------------------------------------------------------------------|
| `/bbq/business/v1/menu-options?category={category}` | GET    | Recupera todas las opciones de menú. Se filtra por categoría si se envía el query param category. |
| `/bbq/business/v1/menu-options/{id}`                | GET    | Recupera una opción de menú por id.                                                               |
| `/bbq/business/v1/menu-options`                     | POST   | Almacena una nueva opción de menú.                                                                |
| `/bbq/business/v1/menu-options/{id}`                | PUT    | Actualiza un registro de opción de menú.                                                          |
| `/bbq/business/v1/menu-options/{id}`                | DELETE | Elimina un registro de opción de menú.                                                            |

## 3.2. Business API Dining Room Order V1
Gesiona los pedidos que se realizan en el comedor.

| Endpoint                                                        | Método | Descripción                                    |
|-----------------------------------------------------------------|--------|------------------------------------------------|
| `/bbq/business/v1/dining-room-orders?tableNumber={tableNumber}` | GET    | Recupera los pedidos de una mesa.              |
| `/bbq/business/v1/dining-room-orders/{id}`                      | PATCH  | Agrega opciones de menú al pedido de una mesa. |

## 3.3. Business API Invoice V1
Gesiona las facturas asociadas a los pedidos realizados en el comedor.

| Endpoint                                              | Método | Descripción                                         |
|-------------------------------------------------------|--------|-----------------------------------------------------|
| `/bbq/business/v1/invoices?tableNumber={tableNumber}` | GET    | Recupera la factura asociada al pedido de una mesa. |
| `/bbq/business/v1/invoices/send-to-pay`               | POST   | Envía a pagar la factura.                           |

## 3.4. Business API Payment V1
Lista los pagos asociadas a los pedidos realizados en el comedor.

| Endpoint                      | Método | Descripción              |
|-------------------------------|--------|--------------------------|
| `/bbq/business/v1/payments`   | GET    | Recupera todos los pagos | 

## 3.5. Experience API Kitchen Order V1
Lista las órdenes para cocina.

| Endpoint                                                      | Método | Descripción                      |
|---------------------------------------------------------------|--------|----------------------------------|
| `/bbq/experience/v1/kitchen-orders?tableNumber={tableNumber}` | GET    | Recupera las órdenes para cocina | 


