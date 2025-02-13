package com.dw.AccountBook.domain;

import com.dw.AccountBook.presentation.dto.accounting.AccountingDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Accounting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(nullable = false)
    private Long amount;

    @Column
    private String description;

    @Column(nullable = false)
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_code")
    private User user;

    public static Accounting toEntity(AccountingDto accountingDto, User user) {
        Accounting accounting = new Accounting();
        accounting.setCategory(accountingDto.getCategory());
        accounting.setAmount(accountingDto.getAmount());
        accounting.setDescription(accounting.getDescription());
        accounting.setDate(accountingDto.getDate());
        accounting.setUser(user);

        return accounting;
    }
}
