package com.grapeup.webapp.testWebApp.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/")
public class HomeController {
    @Value("${app.version}")
    private String appVersion;

    @GetMapping
    public Map<String, String> getStatus() {
        return Map.of("appVersion", appVersion);
    }
}
