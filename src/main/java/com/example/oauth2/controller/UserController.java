package com.example.oauth2.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
  //  @PreAuthorize("hasAuthority('OIDC_USER')")
    @GetMapping("/cabinet")
    public String home(){
        return "home";
    }

}
