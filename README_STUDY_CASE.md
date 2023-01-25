# Caso de estudio: Reactive BBQ Restaurant
`<autor>`: Miguel Rodrigo Armas Abt

- [1. Expertos en el dominio](#1-expertos-en-el-dominio)
- [2. Bounded contexts](#2-bounded-contexts)

> Reactive BBQ es un restaurante que se enfoca en sabores tradicionales de barbacoa. Comenzó como una tienda familiar y 
> finalmente comenzó a crecer. Ellos habrían abierto un par de otras ubicaciones, luego tal vez se mudaron a algunas 
> ciudades diferentes, luego se mudaron por todo el país y ahora están en el punto en el que realmente se han 
> globalizado.
>
> El software es una gran parte de su negocio, sin embargo, lo que sucedió es que, el restaurante realmente creció y el 
> software creció orgánicamente con él, simplemente atornillaron cosas en diferentes lugares hasta el punto en que ahora
> tienen una aplicación que hace varias cosas. Hace de todo, desde la gestión de inventario hasta los precios del menú, 
> las entregas y los pedidos y reservas online. Todos estos se han agrupado en un solo sistema y lo que están 
> descubriendo ahora es que ese sistema realmente está luchando por su propio peso. Tienen muchos procesos heredados 
> relacionados con ese sistema antiguo y algunos de esos procesos heredados requieren que el sistema se apague durante 
> un período de tiempo. Cuando, por supuesto, el sistema se desactiva, significa que no pueden hacer cosas en sus 
> restaurantes, por lo que deben tratar de orientar ese tiempo de inactividad a períodos en los que no hay mucha 
> actividad en sus restaurantes o cuando los restaurantes están cerrados. Eso estaba bien cuando todas sus ubicaciones 
> estaban en América del Norte, pero a medida que se han globalizado, cada vez es más difícil encontrar esos períodos de
> tiempo en los que pueden tener tiempo de inactividad.
>
> Entonces El Reactive BBQ Restaurant, ahora está buscando arquitecturas para tratar de ayudar a resolver ese problema, 
> ya que está experimentando una importante actualización de su software. Buscan modernizar su monolito heredado 
> mediante la creación de nuevas piezas de la aplicación como microservicios.

## 1. Expertos en el dominio
> Tras hablar con los expertos en el dominio "restaurante" e intentar entender su vocabulario para usarlo en nuestro 
> modelo pudimos identificar las siguientes actividades utilizando la notación sujeto-verbo-objeto.

- Anfitrión
    - El anfitrión verifica las reservas actuales.
    - El anfitrión crea una reserva para un cliente.
    - El anfitrión asienta al cliente con reserva.

- Mesero
    - El mesero toma el pedido.
    - El mesero entrega el pedido.
    - El mesero cobra el pago de un pedido.
  
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

## 2. Bounded contexts
> De acuerdo a los objetos definidos en las actividades anteriores se identificaron los siguientes bounded contexts y 
> algunas palabras de sus lenguajes ubicuos.

- Reservation: reservation, table, customer, time, location
- Payment: credit, debit, cash
- Order: order, cook, notification, delivery, tip
- Menu: drink, plate, items
