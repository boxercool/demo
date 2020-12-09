package com.poorvika.controller;

import io.micronaut.context.env.Environment;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

import javax.inject.Inject;

@Controller("/Micro")
public class TestController {

    @Inject
    private Environment environment;

    @Get("/test")

    public String index() {
        String message = environment.getProperty("demo.Salt", String.class).get();
        return "Welcome to test world" ;
    }
}
