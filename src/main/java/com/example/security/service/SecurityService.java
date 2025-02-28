package com.example.security.service;

import com.example.security.domain.User;
import com.example.security.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SecurityService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public SecurityService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

//    public boolean authenticate(String username, String password){
//        Optional<User> userOpt = userRepository.findByUsername(username);
//        return userOpt.isPresent() && userOpt.get().getPassword().equals(password);
//    }

    
    //로그인 로직
//    public boolean authenticate(String username, String password){
//        Optional<User> userOpt = userRepository.findByUsername(username);
//        if(userOpt.isEmpty()){
//            return false;
//        }
//        User user = userOpt.get();
//        boolean matches = passwordEncoder.matches(password, user.getPassword()); // matches 메소드를 사용하여 비밀번호 일치 여부 확인
//        return matches;
//    }


    public boolean authenticate(String username, String password){
        try{
         Authentication authentication = authenticationManager.authenticate(
                 new UsernamePasswordAuthenticationToken(username,password) // UsernamePasswordAuthenticationToken 객체를 생성하여 authenticate 메소드에 전달
         );
            return authentication.isAuthenticated(); // 인증여부에 대한 성공인지 실패인지 대한 검사를 실행
        }catch(AuthenticationException e){
            return false;

        }
    }
    
    //회원가입 로직
    public void registerUser(User user){
        user.setRole("ADMIN");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }
}
