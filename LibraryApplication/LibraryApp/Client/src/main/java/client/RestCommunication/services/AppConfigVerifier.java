package client.RestCommunication.services;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class AppConfigVerifier {

    @Bean
    public CommandLineRunner printConfig(Environment env) {
        return args -> {
            String serverPort = env.getProperty("server.port");
            System.out.println("Effective server port: " + serverPort);
        };
    }
}
