package ch.dulce.camel.rest;

import ch.dulce.camel.config.repo.ConfigRepo;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.camel.builder.RouteBuilder;

@ApplicationScoped
public class RoutesResource extends RouteBuilder {

    @Inject
    ConfigRepo configRepo;

    @Override
    public void configure() throws Exception {

        rest("/api")
                .get()
                .routeId("getRouteIds")
                .to("direct:routes");
                
        from("direct:routes")
                .log(configRepo.routeIds().toString());

    }

}
