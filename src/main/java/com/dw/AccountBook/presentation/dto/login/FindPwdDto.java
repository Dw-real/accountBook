package com.dw.AccountBook.presentation.dto.login;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class FindPwdDto {
    private String id;
    @Size(min = 8, max = 20, message = "비밀번호는 8~20자 이내로 입력해야 합니다.")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>])[A-Za-z\\d!@#$%^&*(),.?\":{}|<>]{8,20}$",
            message = "비밀번호 형식을 확인해주세요.")
    private String newPwd;
}
