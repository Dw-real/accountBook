package com.dw.AccountBook.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userCode;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(nullable = false)
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;

    @Column(length = 15, nullable = false, unique = true)
    private String id;

    @Column(length = 60, nullable = false)
    private String pwd;
}
