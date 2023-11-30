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

