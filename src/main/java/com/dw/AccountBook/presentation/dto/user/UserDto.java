package com.dw.AccountBook.presentation.dto.user;

import com.dw.AccountBook.domain.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserDto {
    private Long userCode;
    private String name;
    private String email;
    private String id;
    @Size(min = 8, max = 20, message = "비밀번호는 8~20자 이내로 입력해야 합니다.")
    @NotBlank(message = "비밀번호는 필수 입력 칸입니다.")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>])[A-Za-z\\d!@#$%^&*(),.?\":{}|<>]{8,20}$",
            message = "비밀번호 형식을 확인해주세요.")
    private String pwd;

    public static User toEntity(UserDto userDto) {
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setId(userDto.getId());
        user.setPwd(userDto.getPwd());

        return user;
    }

    public static UserDto toDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setUserCode(user.getUserCode());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setId(user.getId());
        userDto.setPwd(user.getPwd());

        return userDto;
    }
}
