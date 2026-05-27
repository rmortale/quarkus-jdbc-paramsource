package ch.dulce.camel.config;

import com.ibm.mq.jakarta.jms.MQConnectionFactory;
import com.ibm.msg.client.jakarta.wmq.WMQConstants;
import io.smallrye.common.annotation.Identifier;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSException;
import org.apache.camel.spi.RouteTemplateParameterSource;
import org.eclipse.microprofile.config.ConfigProvider;

public class IbmConfig {

  @Inject
  ParamSource paramSource;

  @Identifier("ibmConnectionFactory")
  public ConnectionFactory createConnectionFactory() throws JMSException {
    MQConnectionFactory mq = new MQConnectionFactory();
    mq.setHostName(ConfigProvider.getConfig().getValue("ibm.mq.host", String.class));
    mq.setPort(ConfigProvider.getConfig().getValue("ibm.mq.port", Integer.class));
    mq.setChannel(ConfigProvider.getConfig().getValue("ibm.mq.channel", String.class));
    mq.setQueueManager(ConfigProvider.getConfig().getValue("ibm.mq.queueManagerName", String.class));
    mq.setTransportType(WMQConstants.WMQ_CM_CLIENT);
    mq.setStringProperty(WMQConstants.USERID, ConfigProvider.getConfig().getValue("ibm.mq.username", String.class));
    mq.setStringProperty(WMQConstants.PASSWORD, ConfigProvider.getConfig().getValue("ibm.mq.password", String.class));
    return mq;
  }

  @Produces
  public RouteTemplateParameterSource getRouteTemplateParameterSource() {
    return paramSource;
  }
}
