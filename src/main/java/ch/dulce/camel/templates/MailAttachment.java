package ch.dulce.camel.templates;

import jakarta.enterprise.context.ApplicationScoped;
import java.nio.file.Path;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.endpoint.EndpointRouteBuilder;
import org.apache.camel.component.mail.SplitAttachmentsExpression;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class MailAttachment extends EndpointRouteBuilder {

    public static final String POP3_ATTACH_DOWNLOADER = "pop3AttachmentsDownloader";

    @ConfigProperty(name = "java.io.tmpdir")
    private String tmpDir;

    @Override
    public void configure() throws Exception {

        routeTemplate(POP3_ATTACH_DOWNLOADER)
                .templateParameter("pop3username")
                .templateParameter("pop3password")
                .templateParameter("pop3hostname")
                .templateParameter("pop3port")
                .templateParameter("pop3downloaddir", Path.of(tmpDir, "mails").toString())
                .templateParameter("pop3delay", "60000")
                .templateParameter("pop3fetchsize", "100")
                .templateParameter("autostartup", "true")
                .from(pop3("{{pop3hostname}}:{{pop3port}}")
                        .username("{{pop3username}}")
                        .password("{{pop3password}}")
                        .delay("{{pop3delay}}")
                        .runLoggingLevel(LoggingLevel.INFO)
                        .advanced().fetchSize("{{pop3fetchsize}}")
                        .handleDuplicateAttachmentNames("uuidSuffix"))
                .autoStartup("{{autostartup}}")
                .split(method(new SplitAttachmentsExpression())).streaming()
                .to(file("{{pop3downloaddir}}").fileName("${header.CamelSplitAttachmentId}"));
    }

}
