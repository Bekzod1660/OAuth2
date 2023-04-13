package com.example.oauth2.controller;

import com.example.oauth2.model.dto.UserLoginDto;
import com.example.oauth2.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    @GetMapping("")
    private String login(){
        return "login";
    }
    @GetMapping("/register")
    private String register(){
        return "register";
    }
    @PostMapping("/login")
    private String login2(@ModelAttribute UserLoginDto userLoginDto){
        String username = userLoginDto.getUsername();
        String password = userLoginDto.getPassword();
        userService.checkUserName(username,password);
        return "home";
    }



}
