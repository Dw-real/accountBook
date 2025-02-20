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
    private Type type;

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
        accounting.setType(accountingDto.getType());
        accounting.setCategory(accountingDto.getCategory());
        accounting.setAmount(accountingDto.getAmount());
        accounting.setDescription(accountingDto.getDescription());
        accounting.setDate(accountingDto.getDate());
        accounting.setUser(user);

        return accounting;
    }
    
    public static void updateEntity(Accounting accounting, AccountingDto accountingDto, User user) {
        accounting.setType(accountingDto.getType());
        accounting.setCategory(accountingDto.getCategory());
        accounting.setAmount(accountingDto.getAmount());
        accounting.setDescription(accountingDto.getDescription());
        accounting.setDate(accountingDto.getDate());
        accounting.setUser(user);
    }
}
