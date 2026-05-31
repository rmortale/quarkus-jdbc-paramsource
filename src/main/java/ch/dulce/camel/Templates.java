package ch.dulce.camel;

import jakarta.enterprise.context.ApplicationScoped;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.endpoint.EndpointRouteBuilder;
import org.apache.camel.support.builder.Namespaces;

@ApplicationScoped
public class Templates extends EndpointRouteBuilder {

  private static final String CACHE_LEVEL_NAME = "CACHE_CONSUMER";
  private static final String DEFAULT_MAX_CONSUMERS = "10";
  private static final String DEFAULT_JMS_CONNECTION_FACTORY = "<default>";
  private static final String IN_CONNECTION_FACTORY = "inConnectionFactory";
  private static final String OUT_CONNECTION_FACTORY = "outConnectionFactory";
  private static final String WRAPBODY_TRANSFORMATION = "wrapbodyTransformation";
  private static final String MAX_CONSUMERS = "maxConsumers";
  private static final String INQUEUE = "inqueue";
  private static final String OUTQUEUE = "outqueue";
  private static final String UPPERCASE_TRANSFORMATION = "uppercaseTransformation";
  private static final String EXTRACT_BODY_TRANSFORMATION = "extractBodyTransformation";

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


    routeTemplate(WRAPBODY_TRANSFORMATION)
        .templateParameter(INQUEUE)
        .templateParameter(OUTQUEUE)
        .templateParameter(IN_CONNECTION_FACTORY, DEFAULT_JMS_CONNECTION_FACTORY)
        .templateParameter(OUT_CONNECTION_FACTORY, DEFAULT_JMS_CONNECTION_FACTORY)
        .templateParameter(MAX_CONSUMERS, DEFAULT_MAX_CONSUMERS)
        .from(jms("{{%s}}".formatted(INQUEUE))
          .transacted(true)
          .cacheLevelName(CACHE_LEVEL_NAME)
          .maxConcurrentConsumers("{{%s}}".formatted(MAX_CONSUMERS)).connectionFactory("{{%s}}".formatted(IN_CONNECTION_FACTORY))
          .advanced().lazyCreateTransactionManager(false))
        .transform().simple(MESSAGE)
        .log(LoggingLevel.DEBUG, WRAPBODY_TRANSFORMATION, "${body}")
        .to(jms("{{%s}}".formatted(OUTQUEUE)).connectionFactory("{{%s}}".formatted(OUT_CONNECTION_FACTORY)));

    routeTemplate(UPPERCASE_TRANSFORMATION)
        .templateParameter(INQUEUE)
        .templateParameter(OUTQUEUE)
        .templateParameter(IN_CONNECTION_FACTORY, DEFAULT_JMS_CONNECTION_FACTORY)
        .templateParameter(OUT_CONNECTION_FACTORY, DEFAULT_JMS_CONNECTION_FACTORY)
        .templateParameter(MAX_CONSUMERS, DEFAULT_MAX_CONSUMERS)
        .from(jms("{{%s}}".formatted(INQUEUE))
            .transacted(true)
            .cacheLevelName(CACHE_LEVEL_NAME)
            .maxConcurrentConsumers("{{%s}}".formatted(MAX_CONSUMERS)).connectionFactory("{{%s}}".formatted(IN_CONNECTION_FACTORY))
            .advanced().lazyCreateTransactionManager(false))
        .transform().simple("${uppercase()}")
        .log(LoggingLevel.DEBUG, UPPERCASE_TRANSFORMATION, "${body}")
        .to(jms("{{%s}}".formatted(OUTQUEUE)).connectionFactory("{{%s}}".formatted(OUT_CONNECTION_FACTORY)));

    routeTemplate(EXTRACT_BODY_TRANSFORMATION)
        .templateParameter(INQUEUE)
        .templateParameter(OUTQUEUE)
        .templateParameter("splitExpr")
        .templateParameter(IN_CONNECTION_FACTORY, DEFAULT_JMS_CONNECTION_FACTORY)
        .templateParameter(OUT_CONNECTION_FACTORY, DEFAULT_JMS_CONNECTION_FACTORY)
        .templateParameter(MAX_CONSUMERS, DEFAULT_MAX_CONSUMERS)
        .from(jms("{{%s}}".formatted(INQUEUE))
            .transacted(true)
            .cacheLevelName(CACHE_LEVEL_NAME)
            .maxConcurrentConsumers("{{%s}}".formatted(MAX_CONSUMERS)).connectionFactory("{{%s}}".formatted(IN_CONNECTION_FACTORY))
            .advanced().lazyCreateTransactionManager(false))
        .split().xtokenize("{{splitExpr}}", 'u', new Namespaces())
        .log(LoggingLevel.DEBUG, EXTRACT_BODY_TRANSFORMATION, "${body}")
        .to(jms("{{%s}}".formatted(OUTQUEUE)).connectionFactory("{{%s}}".formatted(OUT_CONNECTION_FACTORY)));
  }

}
