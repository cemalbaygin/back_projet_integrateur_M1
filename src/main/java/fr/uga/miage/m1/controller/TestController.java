package fr.uga.miage.m1.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/public/hello")
    public String hello() {
        return "hello world !";
    }

    @GetMapping("/private/hello")
    public String privHello() {
        return "hello world !";
    }
}
