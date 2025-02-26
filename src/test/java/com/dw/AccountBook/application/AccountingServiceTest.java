package com.dw.AccountBook.application;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.dw.AccountBook.domain.Accounting;
import com.dw.AccountBook.domain.Category;
import com.dw.AccountBook.domain.Type;
import com.dw.AccountBook.domain.User;

import com.dw.AccountBook.infrastructure.AccountingRepository;
import com.dw.AccountBook.infrastructure.UserRepository;
import com.dw.AccountBook.presentation.dto.accounting.AccountingDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class AccountingServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AccountingRepository accountingRepository;

    @InjectMocks
    private AccountingService accountingService;

    private User user;
    private AccountingDto accountingDto;

    @BeforeEach
    void setUp() {
        // Set up a mock User
        user = new User();
        user.setUserCode(1L);
        user.setName("han");
        user.setEmail("han@naver.com");
        user.setId("han1234");
        user.setPwd("pwd1234");

        accountingDto = new AccountingDto();
        accountingDto.setUserCode(1L);
        accountingDto.setType(Type.EXPENSE); // 예시 타입
        accountingDto.setCategory(Category.FOOD); // 예시 카테고리
        accountingDto.setAmount(10000L);
        accountingDto.setDescription("점심");
        accountingDto.setDate(LocalDate.now());
    }

    @Test
    @DisplayName("가계부 내역 등록 테스트")
    void registerTest_userExists() {
        // Given
        when(userRepository.findById(accountingDto.getUserCode())).thenReturn(Optional.of(user));

        // When
        accountingService.register(accountingDto);

        // Then
        verify(accountingRepository, times(1)).save(any(Accounting.class));  // save가 한 번 호출되어야 함
    }

    @Test
    @DisplayName("존재하지 않는 계정으로 가계부 내역 작성을 하면 등록되지 않아야 한다.")
    void registerTest_userNotFound() {
        // Given
        when(userRepository.findById(accountingDto.getUserCode())).thenReturn(Optional.empty());

        // When
        accountingService.register(accountingDto);

        // Then
        verify(accountingRepository, times(0)).save(any(Accounting.class));  // save는 호출되지 않아야 함
    }
}
