package ch.dulce.camel;

import jakarta.enterprise.context.ApplicationScoped;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.endpoint.EndpointRouteBuilder;
import org.apache.camel.support.builder.Namespaces;

@ApplicationScoped
public class Templates extends EndpointRouteBuilder {

  private static final String CACHE_LEVEL_NAME = "CACHE_CONSUMER";
  private static final String DEFAULT_MAX_CONSUMERS = "10";

  private static final String MESSAGE = """
      <object>
        <header>
          <system>${header.system}</system>
          <serviceid>${header.serviceid}</serviceid>
        </header>
        <body>${body}</body>
      </object>
      """;

  @Override
  public void configure() throws Exception {

    routeTemplate("wrapbodyTransformation")
        .templateParameter("inqueue")
        .templateParameter("outqueue")
        .templateParameter("inConnectionFactory", "<default>")
        .templateParameter("outConnectionFactory", "<default>")
        .templateParameter("maxConsumers", DEFAULT_MAX_CONSUMERS)
        .from(jms("{{inqueue}}")
          .transacted(true)
          .cacheLevelName(CACHE_LEVEL_NAME)
          .maxConcurrentConsumers("{{maxConsumers}}").connectionFactory("{{inConnectionFactory}}")
          .advanced().lazyCreateTransactionManager(false))
        .transform().simple(MESSAGE)
        .log(LoggingLevel.DEBUG, "wrapbodyTransformation", "${body}")
        .to(jms("{{outqueue}}").connectionFactory("{{outConnectionFactory}}"));

    routeTemplate("uppercaseTransformation")
        .templateParameter("inqueue")
        .templateParameter("outqueue")
        .templateParameter("maxConsumers", DEFAULT_MAX_CONSUMERS)
        .from(jms("{{inqueue}}")
            .transacted(true)
            .cacheLevelName(CACHE_LEVEL_NAME)
            .maxConcurrentConsumers("{{maxConsumers}}")
            .advanced().lazyCreateTransactionManager(false))
        .transform().simple("${uppercase()}")
        .log(LoggingLevel.DEBUG, "uppercaseTransformation", "${body}")
        .to(jms("{{outqueue}}"));

    routeTemplate("extractBodyTransformation")
        .templateParameter("inqueue")
        .templateParameter("outqueue")
        .templateParameter("splitExpr")
        .templateParameter("maxConsumers", DEFAULT_MAX_CONSUMERS)
        .from(jms("{{inqueue}}")
            .transacted(true)
            .cacheLevelName(CACHE_LEVEL_NAME)
            .maxConcurrentConsumers("{{maxConsumers}}")
            .advanced().lazyCreateTransactionManager(false))
        .split().xtokenize("{{splitExpr}}", 'u', new Namespaces())
        .log(LoggingLevel.DEBUG, "extractBodyTransformation", "${body}")
        .to(jms("{{outqueue}}"));
  }

}
