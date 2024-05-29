package server;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import server.model.Librarian;
import server.persistance.implementations.LibrarianRepository;
import server.service.util.PasswordEncryption;

@SpringBootApplication
//@EnableTransactionManagement
public class ServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }
    @Bean
    public CommandLineRunner run() {
        return args -> {
            System.out.println("Server started");
            LibrarianRepository librarianRepository = new LibrarianRepository();
            librarianRepository.getAll().forEach(System.out::println);
            PasswordEncryption.hashPassword("ana");
        };

    }
}
