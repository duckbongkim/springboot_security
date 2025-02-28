package com.example.security.domain;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import java.sql.Timestamp;

@Entity
@Table(name="user")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String username;
    private String password;
    private String email;
    private String role;

    @CreationTimestamp // 생성일자 자동 입력
    private Timestamp createDate;
}
