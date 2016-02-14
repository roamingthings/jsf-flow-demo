# Enter a JSF flow context passing a parameter or value

This project demonstrates how to start a JSF flow context and pass a (request) parameter or value as a request query parameter of the
 flow.

## JSF Flow Scope and inbound parameters

In JSF 2.2 the `Flow scope` has been a great addition. It is now easier to implement wizard like dialogs that are
 spread over multiple views.

 Unfortunately although it is possible to pass parameters between nested flow scopes (_inbound_ and _outbound_
  parameters) there seems to be no standard way of passing a a (request query) parameter to a starting a flow.

  This project demonstrates how to pass a query parameter to a flow so it can be started by performing a GET request like
  `/flow-demo/registration/registration.xhtml?id=5`

## How does it work

When outside a flow a call to a bean that is part of the flow scope will lead to a `ContextNotActiveException`. So it
is not possible to have a view action or view parameter inside a `<f:metadata>` section of the starting view. However
it is possible to call a view action of a request scoped bean which itself retrieves a view parameter and start the
flow by returning the outcome / id of the flow.

 When inside the flow another CDI bean that is bound to the flow scope can inject the request bean and retrieve any
 value or parameter that has been captured from the initial request.
