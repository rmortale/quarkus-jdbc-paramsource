package ch.dulce.camel.rest;

import ch.dulce.camel.config.repo.ConfigRepo;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;

import java.util.Map;
import java.util.Set;

@Path("/routes")
public class RoutesResource {


  @Inject
  ConfigRepo configRepo;

  @GET
  public Set<String> routeIds() {
    return configRepo.routeIds();
  }

  @Path("/{routeid}")
  @GET
  public Map<String, Object> findRouteById(@PathParam("routeid") String routeId) {
    return configRepo.parameters(routeId);
  }

}
