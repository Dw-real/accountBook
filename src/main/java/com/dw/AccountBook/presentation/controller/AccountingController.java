package com.dw.AccountBook.presentation.controller;

import com.dw.AccountBook.application.AccountingService;
import com.dw.AccountBook.presentation.ApiResponse;
import com.dw.AccountBook.presentation.dto.accounting.AccountingDto;
import com.dw.AccountBook.presentation.dto.user.UserDto;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/accounting")
@CrossOrigin("*")
public class AccountingController {

    private final AccountingService accountingService;

    @Autowired
    public AccountingController(AccountingService accountingService) {
        this.accountingService = accountingService;
    }

    /*
        작성
     */
    @PostMapping("/post")
    public ResponseEntity<?> register(@RequestBody AccountingDto accountingDto) {
        accountingService.register(accountingDto);

        return ResponseEntity.ok(ApiResponse.success("등록 성공", accountingDto));
    }

    /*
        전체 내역 보기
     */
    @GetMapping("/getMore")
    public ResponseEntity<?> getAccountingData(HttpSession session,
                                                                 @RequestParam(defaultValue = "0") int page,
                                                                 @RequestParam(defaultValue = "20") int size) {
        UserDto loggedInUser = (UserDto) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Page.empty());
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("date").descending());
        Page<AccountingDto> accountingData = accountingService.findAllByUserCode(loggedInUser.getUserCode(), pageable);

        return ResponseEntity.ok(accountingData);
    }


    /*
        월별 내역 보기
     */
    @GetMapping("/getMonthly")
    public ResponseEntity<?> getAccountingByMonthly(HttpSession session,
                                                      @RequestParam int year,
                                                      @RequestParam int month,
                                                      @RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "20") int size) {

        UserDto loggedInUser = (UserDto) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Page.empty());
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("date").descending());
        Page<AccountingDto> accountingData = accountingService.findByUserCodeAndMonth(loggedInUser.getUserCode(), year, month, pageable);

        return ResponseEntity.ok(accountingData);
    }

    /*
        월별 분석 보기
     */
    @GetMapping("/getMonthlyData")
    public ResponseEntity<?> getMonthlyData(HttpSession session, @RequestParam int year, @RequestParam int month) {
        UserDto loggedInUser = (UserDto) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.emptyList());
        }

        List<AccountingDto> monthlyData = accountingService.findByUserCodeAndMonth(loggedInUser.getUserCode(), year, month);
        return ResponseEntity.ok(monthlyData);
    }


    /*
        수정
     */
    @PatchMapping("/update")
    public ResponseEntity<?> update(@RequestBody AccountingDto accountingDto, Model model) {
        AccountingDto updatedAccounting = accountingService.update(accountingDto);

        model.addAttribute("accounting", updatedAccounting);

        return ResponseEntity.ok(ApiResponse.success("수정 성공", updatedAccounting));
    }

    /*
        삭제
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        accountingService.delete(id);
        AccountingDto deletedAccounting = accountingService.findById(id);

        return ResponseEntity.ok(ApiResponse.success("삭제 성공", deletedAccounting));
    }
}
