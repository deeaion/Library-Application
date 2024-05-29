package client.RestCommunication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Properties;

@Component
public class PortInitializer implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    private static final Logger logger = LoggerFactory.getLogger(PortInitializer.class);

    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        logger.debug("PortInitializer invoked");
        ConfigurableEnvironment environment = event.getEnvironment();
        MutablePropertySources propertySources = environment.getPropertySources();

        int startingPort = Integer.parseInt(environment.getProperty("custom.server.starting-port", "55556"));
        System.out.println("Starting port: " + startingPort);
        int port = findAvailablePort(startingPort);

        logger.info("Setting server port to {}", port);

        Properties props = new Properties();
        props.put("server.port", port);
        propertySources.addFirst(new PropertiesPropertySource("dynamicPort", props));
    }

    private int findAvailablePort(int startingPort) {
        int port = startingPort;
        while (!isPortAvailable(port)) {
            port++;
        }
        return port;
    }

    private boolean isPortAvailable(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            return true;
        } catch (IOException ex) {
            return false;
        }
    }
}
