package com.dw.AccountBook.application;

import com.dw.AccountBook.infrastructure.UserRepository;
import com.dw.AccountBook.presentation.dto.user.UserDto;
import com.dw.AccountBook.presentation.dto.user.UserIdRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @BeforeEach
    void setUp() {
        bCryptPasswordEncoder = new BCryptPasswordEncoder();
    }

    @BeforeEach
    void cleanUp() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("계정 생성 및 조회 기능 확인")
    void createUserTest() {
        // Given
        UserDto userDto = new UserDto();
        userDto.setName("han");
        userDto.setEmail("han@naver.com");
        userDto.setId("han1234");
        userDto.setPwd("dwhan1111");

        // When
        UserDto createdUser = userService.create(userDto);
        boolean logIn = userService.logIn(userDto.getId(), userDto.getPwd());
        UserDto foundUser = userService.findById(userDto.getId());
        UserDto foundUser2 = userService.findByInfo(new UserIdRequestDto(userDto.getName(), userDto.getEmail()));

        // Then
        assertNotNull(createdUser);
        assertNotNull(createdUser.getUserCode());
        assertEquals(userDto.getName(), createdUser.getName());
        assertEquals(userDto.getEmail(), createdUser.getEmail());
        assertEquals(userDto.getId(), createdUser.getId());

        // 비밀번호는 암호화되어 저장되므로, 원본 비밀번호와 동일한지 직접 비교할 수 없음
        assertNotEquals(userDto.getPwd(), createdUser.getPwd());
        assertTrue(bCryptPasswordEncoder.matches("dwhan1111", createdUser.getPwd()));

        // 로그인
        assertTrue(logIn);

        // 아이디로 조회
        assertNotNull(foundUser);
        assertNotNull(foundUser.getUserCode());
        assertEquals(foundUser.getName(), createdUser.getName());
        assertEquals(foundUser.getEmail(), createdUser.getEmail());
        assertEquals(foundUser.getId(), createdUser.getId());

        // 이름과 이메일로 계정 정보 찾기
        assertNotNull(foundUser2);
        assertNotNull(foundUser2.getUserCode());
        assertEquals(foundUser2.getName(), createdUser.getName());
        assertEquals(foundUser2.getEmail(), createdUser.getEmail());
        assertEquals(foundUser2.getId(), createdUser.getId());
    }

    @Test
    @DisplayName("중복 아이디가 아닌 경우 false를 반환해야 한다.")
    void checkIdTest() {
        // Given
        String id = "han123";

        // When
        Boolean existId = userRepository.existsById(id);

        // Then
        assertFalse(existId);
    }
}
