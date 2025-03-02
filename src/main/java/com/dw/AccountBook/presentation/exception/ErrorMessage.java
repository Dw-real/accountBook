package com.dw.AccountBook.presentation.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ErrorMessage {
    private List<String> errors;
}
