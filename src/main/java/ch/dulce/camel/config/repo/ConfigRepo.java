package ch.dulce.camel.config.repo;

import ch.dulce.camel.config.ParamConfig;
import io.quarkiverse.fluentjdbc.runtime.RecordMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.codejargon.fluentjdbc.api.FluentJdbc;
import org.codejargon.fluentjdbc.api.mapper.Mappers;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class ConfigRepo {

  private static final String GET_ROUTES_SQL = "select routeid from tpl_config where agentid = ? and config_version = ?";

  @ConfigProperty(name = "app.agent.config.id")
  private String agentId;
  @ConfigProperty(name = "app.agent.config.version")
  private String configVersion;

  private static final RecordMapper<ParamConfig> CONFIG_RECORD_MAPPER = new RecordMapper(ParamConfig.class);

  @Inject
  private FluentJdbc jdbc;

  public Set<String> getAllRouteIds() {
    return jdbc.query().select(GET_ROUTES_SQL).params(agentId, configVersion).setResult(Mappers.singleString());
  }

  public Map<String, Object> getConfig(String routeId) {
    return jdbc.query().select("select * from tpl_config where routeid = ?")
        .params(routeId)
        .listResult(CONFIG_RECORD_MAPPER)
        .stream().collect(Collectors.toMap(ParamConfig::configKey, ParamConfig::configValue));
  }
}
