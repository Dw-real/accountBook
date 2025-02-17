package com.dw.AccountBook.presentation.dto.accounting;

import com.dw.AccountBook.domain.Accounting;
import com.dw.AccountBook.domain.Category;
import com.dw.AccountBook.domain.Type;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AccountingDto {
    private Long id;
    private Type type;
    private Category category;
    private Long amount;
    private String description;
    private LocalDate date;
    private Long userCode;

    public static AccountingDto toDto(Accounting accounting) {
        AccountingDto accountingDto = new AccountingDto();
        accountingDto.setId(accounting.getId());
        accountingDto.setType(accounting.getType());
        accountingDto.setCategory(accounting.getCategory());
        accountingDto.setAmount(accounting.getAmount());
        accountingDto.setDescription(accounting.getDescription());
        accountingDto.setDate(accounting.getDate());
        accountingDto.setUserCode(accounting.getUser().getUserCode());


        return accountingDto;
    }
}
