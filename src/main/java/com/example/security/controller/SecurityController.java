package com.example.security.controller;

import com.example.security.domain.User;
import com.example.security.dto.request.LoginRequest;
import com.example.security.repository.UserRepository;
import com.example.security.service.SecurityService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.hibernate.query.criteria.JpaCteCriteriaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class SecurityController {
    private final SecurityService securityService;

    @Autowired
    public SecurityController(SecurityService securityService) {
        this.securityService = securityService;
    }


    @GetMapping("/user")
    public String user(){
        return "User Page";
    }

    @GetMapping("/user/me")
    public String username(){
        return "username page";
    }

    @GetMapping("/admin")
    public String admin(){
        return "Admin Page";
    }

    @GetMapping("/admin/me")
    public String adminname(){
        return "adminname page";
    }

//    @GetMapping("/")
//    public String root(){
//        return "Root Page";
//
//    }

    @GetMapping("/")
    public String getCurrentUserRole(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null || !authentication.isAuthenticated()){
            return "로그인된 사용자 없음";

        }

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String roles = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(", "));
        return "현재 사용자:" + authentication.getName()+"권한: "+roles;
    }

    @GetMapping("/login") // 로그인 페이지
    public String loginPage() throws IOException{
        Path path = Paths.get("src/main/resources/templates/login.html");
        return Files.readString(path);
    }

    @PostMapping("/login")
    public void login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) throws Exception{
        if(!securityService.authenticate(loginRequest.getUsername(), loginRequest.getPassword())){
            response.sendRedirect("http://localhost:8080/login?error=true");
            return;
        }
        response.sendRedirect("http://localhost:8080/");
    }

    @GetMapping("/signup") // 회원가입 페이지
    public String signPage() throws IOException{
        Path path = Paths.get("src/main/resources/templates/signup.html");
        return Files.readString(path);
    }

    @PostMapping("/signup") // 정보를 다 받와야 하니 User 객체로 받아줌
    public void signup(@RequestBody User user, HttpServletResponse response) throws Exception{
        securityService.registerUser(user);
        response.sendRedirect("http://localhost:8080/login");
    }
}



