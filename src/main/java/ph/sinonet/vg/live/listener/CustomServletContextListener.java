package ph.sinonet.vg.live.listener;

import ph.sinonet.vg.live.model.enums.ProjectType;
import ph.sinonet.vg.live.websocket.parser.MessageDecoder;
import ph.sinonet.vg.live.websocket.parser.MessageEncoder;
import ph.sinonet.vg.live.websocket.server.MainSocketServer;
import ph.sinonet.vg.live.websocket.server.SpringConfiguratorServer;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.websocket.Decoder;
import javax.websocket.Encoder;
import javax.websocket.server.ServerContainer;
import javax.websocket.server.ServerEndpointConfig;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static javax.websocket.server.ServerEndpointConfig.Builder.create;

/**
 * Created by kierpagdato on 7/22/16.
 */
@WebListener
public class CustomServletContextListener implements ServletContextListener {

    private final static String SERVER_CONTAINER_ATTRIBUTE = "javax.websocket.server.ServerContainer";
    private final static String WEBSOCKET_ENDPOINT_CONTEXT = "/sms";

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext container = servletContextEvent.getServletContext();

        final ServerContainer serverContainer = (ServerContainer) container.getAttribute(SERVER_CONTAINER_ATTRIBUTE);
        try {
            serverContainer.addEndpoint(createServerEndpointConfig());
            serverContainer.setDefaultMaxSessionIdleTimeout(20000);
//            serverContainer.setDefaultMaxSessionIdleTimeout(0);
            servletContextEvent.getServletContext().setAttribute("ProjectType", Arrays.asList(ProjectType.values()));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }

    protected ServerEndpointConfig createServerEndpointConfig() {
        final List<Class<? extends Encoder>> encoders = new LinkedList<>();
        final List<Class<? extends Decoder>> decoders = new LinkedList<>();
        encoders.add(MessageEncoder.class);
        decoders.add(MessageDecoder.class);

        final ServerEndpointConfig endpointConfig = create(MainSocketServer.class, WEBSOCKET_ENDPOINT_CONTEXT).configurator(new SpringConfiguratorServer()).encoders(encoders).decoders(decoders).build();
        return endpointConfig;
    }
}
