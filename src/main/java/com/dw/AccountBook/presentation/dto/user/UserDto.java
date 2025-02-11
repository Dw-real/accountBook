package com.dw.AccountBook.presentation.dto.user;

import com.dw.AccountBook.domain.User;
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
