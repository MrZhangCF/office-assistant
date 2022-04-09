package com.baby.wind.office.assistant.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

    @GetMapping("/")
    public String index(){
        return String.valueOf(System.currentTimeMillis());
    }

    @GetMapping("/error")
    public String error(){
        return "error";
    }
}
