package com.dw.AccountBook.presentation.dto.login;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class WithDrawDto {
    private String email;
    private String pwd;
}
