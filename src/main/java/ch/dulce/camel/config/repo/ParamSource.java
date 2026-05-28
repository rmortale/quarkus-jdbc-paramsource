package ch.dulce.camel.config.repo;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.camel.spi.RouteTemplateParameterSource;

import java.util.Map;
import java.util.Set;

@ApplicationScoped
public class ParamSource implements RouteTemplateParameterSource {

  @Inject
  private ConfigRepo repo;


  @Override
  public Map<String, Object> parameters(String routeId) {
    return repo.getConfig(routeId);
  }

  @Override
  public Set<String> routeIds() {
    return repo.getAllRouteIds();
  }



}
