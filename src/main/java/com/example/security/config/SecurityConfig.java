package com.example.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // Spring에 설정 파일입니다.
@EnableWebSecurity //Spring security 활성화
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .csrf(csrf->csrf.disable()) 
                .authorizeHttpRequests(auth->auth // 모든 리퀘스트는 sequrity 가 받도록 명시
                        .requestMatchers("/login","/signup").permitAll() // 인증과 인가를 모두 허용
                        .requestMatchers("/admin/**").hasRole("ADMIN") // ADMIN 권한을 가진 사용자만 접근 가능
                        .requestMatchers("/user/**").hasRole("USER") // USER 권한을 가진 사용자만 접근 가능
                        .anyRequest().authenticated())
                .sessionManagement(session->session.sessionCreationPolicy(
                        SessionCreationPolicy.ALWAYS))
                .formLogin(form->form.disable()) // 기본 로그인폼을 사용안함
                .logout(logout->logout.logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true) // 로그아웃후에 세션만료
                        .deleteCookies("JSESSIONID") // 로그아웃후 쿠키 무효화 // JSESSIONID << 스프링 부트에서 자동적으로 생성되는 쿠키이름
                        .permitAll());

        return http.build();
    }

    @Bean // 패스워드를 암호화 해줌  // 단방향일시 디코더는 필요없음
    public PasswordEncoder passwordEncoder(){
     return new BCryptPasswordEncoder(); 
    }
//      커스텀 인증 로직을 구성할때 UserDetailService를 사용하는 경우
    @Bean // 인증처리 해주는 영역 >> 커스텀으로 로그인을 구성할때 필요함,암호화를 했을때의 로그인을 할때 필요함 // 인증을 자체적으로 할때에는 오버라이딩 필요
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    } // 해당코드를 작성하면 CustomUserDetailsService 를 호출함     // AuthenticationManager.authenticate() 실행되면 UserDetailsService.loadUserByUsername() 실행
}
