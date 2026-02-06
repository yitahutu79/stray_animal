package com.example.strayanimal.user;

import com.example.strayanimal.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "user")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false)
    private String password;

    private String realName;

    @Column(length = 20)
    private String phone;

    private String email;

    private String address;

    @Column(length = 20)
    private String userType; // ADMIN / STAFF / VOLUNTEER / ADOPTER

    @Column(length = 20)
    private String status;   // ENABLED / DISABLED
}

