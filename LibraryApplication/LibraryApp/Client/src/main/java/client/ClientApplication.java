package client;

import client.RestCommunication.PortInitializer;
import javafx.application.Application;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class ClientApplication implements ApplicationRunner {

    public static void main(String[] args) {

        SpringApplicationBuilder builder = new SpringApplicationBuilder(ClientApplication.class);
        builder.application().addListeners(new PortInitializer());
        ConfigurableApplicationContext context = builder.run(args);
    }

    @Override
    public void run(ApplicationArguments args) {


        Application.launch(JavaFXApp.class, args.getSourceArgs());
    }
}
