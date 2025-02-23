package com.harsh.auth.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;



@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {
    @GetMapping("/")
    public ResponseEntity<Map<String, String>> ping() {
        return ResponseEntity.ok(Map.of("message", "Hello From Admin!"));
    }

    // TODO : Implement signup for admin

}
