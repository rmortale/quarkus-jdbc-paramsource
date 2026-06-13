package ch.dulce.camel.config.repo;

import io.quarkiverse.fluentjdbc.runtime.RecordMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.camel.spi.RouteTemplateParameterSource;
import org.codejargon.fluentjdbc.api.FluentJdbc;
import org.codejargon.fluentjdbc.api.mapper.Mappers;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class ConfigRepo implements RouteTemplateParameterSource {

  private static final String GET_ROUTEIDS_SQL = "select routeid from tpl_config where agentid = ? and config_version = ?";
  private static final String GET_PARAMS_SQL = "select * from tpl_config where routeid = ?";
  private static final RecordMapper<ParamConfig> CONFIG_RECORD_MAPPER = new RecordMapper(ParamConfig.class);

  @ConfigProperty(name = "app.agent.config.id")
  private String agentId;
  @ConfigProperty(name = "app.agent.config.version")
  private String configVersion;

  @Inject
  private FluentJdbc jdbc;

  @Override
  public Set<String> routeIds() {
    return jdbc.query().select(GET_ROUTEIDS_SQL).params(agentId, configVersion).setResult(Mappers.singleString());
  }

  @Override
  public Map<String, Object> parameters(String routeId) {
    return jdbc.query().select(GET_PARAMS_SQL)
        .params(routeId)
        .listResult(CONFIG_RECORD_MAPPER)
        .stream().collect(Collectors.toMap(ParamConfig::configKey, ParamConfig::configValue));
  }
}
