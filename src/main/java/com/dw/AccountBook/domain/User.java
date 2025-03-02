package com.dw.AccountBook.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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
    @Size(min = 2, max = 20, message = "이름은 2~20자 이내로 입력해야 합니다.")
    @NotBlank(message = "이름은 필수 입력 칸입니다.")
    private String name;

    @Column(nullable = false)
    @NotBlank
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;

    @Column(length = 15, nullable = false, unique = true)
    @Size(min = 4, max = 15, message = "아이디는 4~15자 이내로 입력해야 합니다.")
    @NotBlank(message = "아이디는 필수 입력 칸입니다.")
    private String id;

    @Column(length = 60, nullable = false)
    @NotBlank(message = "비밀번호는 필수 입력 칸입니다.")
    private String pwd;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Accounting> accountingList = new ArrayList<>();
}
