package ch.dulce.camel;


import jakarta.enterprise.context.ApplicationScoped;
import org.apache.camel.builder.endpoint.EndpointRouteBuilder;

@ApplicationScoped
public class CamelRoutes extends EndpointRouteBuilder {

  @Override
  public void configure() throws Exception {
  }

}
