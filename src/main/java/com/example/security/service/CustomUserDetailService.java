package com.example.security.service;

import com.example.security.domain.User;
import com.example.security.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService { // UserDetailsService 인터페이스를 구현하여 커스텀 UserDetailsService를 생성

    private final UserRepository userRepository;


    public CustomUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    
    // 모든 보안규칙이 정의 되어있어서 오버라이딩을 해줘야함
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println(username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("유저 없음"));

        System.out.println(username+": UserDetailService");

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole())
                .build();
    }


}
