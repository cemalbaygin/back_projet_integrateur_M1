package fr.uga.miage.m1.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class ControllerAuth {

    @GetMapping("/hello")
    public String hello(){
        return "hello world !";
    }
}
