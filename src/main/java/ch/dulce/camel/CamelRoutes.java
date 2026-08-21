package ch.dulce.camel;


import static ch.dulce.camel.templates.MailAttachment.POP3_ATTACH_DOWNLOADER;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.camel.builder.endpoint.EndpointRouteBuilder;

@ApplicationScoped
public class CamelRoutes extends EndpointRouteBuilder {

  @Override
  public void configure() throws Exception {
      
      templatedRoute(POP3_ATTACH_DOWNLOADER)
              .parameter("pop3hostname", "localhost")
              .parameter("pop3port", "14110")
              .parameter("pop3username", "tom")
              .parameter("pop3password", "gugus");
  }

}
