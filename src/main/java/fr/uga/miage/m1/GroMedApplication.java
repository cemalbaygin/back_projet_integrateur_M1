package fr.uga.miage.m1;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.extern.java.Log;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@Log
@OpenAPIDefinition(info = @Info(title = "Gromed API", version = "0.1", description = "L'api de gromed contenant tout les endpoints et les définitions des schémas"))
public class GroMedApplication {
    public static void main(String[] args) {
        SpringApplication.run(GroMedApplication.class, args);
    }
}
