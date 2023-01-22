package fr.uga.miage.m1;

import fr.uga.miage.m1.command.SeederCommand;
import fr.uga.miage.m1.model.Person;
import fr.uga.miage.m1.repository.PersonsRepository;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.java.Log;

@SpringBootApplication
@RestController
@Log
@OpenAPIDefinition(info = @Info(title = "Gromed API", version = "0.1", description = "L'api de gromed contenant tout les endpoints et les définitions des schémas"))
public class MyApplication {

    @Autowired
    PersonsRepository persons;

    @GetMapping(
            value = "/hello",
            produces = "application/json")
    @ResponseBody
    /**
     *
     * @return the first and the last name
     */
    public String testDBConnexion() {
        log.info("testDBConnexion called");
        String answer = "No person found";
        if (persons.count() != 0) {
            Person p = persons.findAll().get(0);
            answer = "\"" + p.getFirstName() + " " + p.getLastName() + "\"";
        }
        return answer;
    }

    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class, args);
    }
}
