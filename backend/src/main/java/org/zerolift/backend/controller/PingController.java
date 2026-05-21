package org.zerolift.backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PingController{

    @RequestMapping("/ping")
    public String ping(){
        return "pong";
    }
}

