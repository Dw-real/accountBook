package com.dw.AccountBook.presentation.controller;

import com.dw.AccountBook.application.AccountingService;
import com.dw.AccountBook.domain.Accounting;
import com.dw.AccountBook.presentation.ApiResponse;
import com.dw.AccountBook.presentation.dto.accounting.AccountingDto;
import com.dw.AccountBook.presentation.dto.user.UserDto;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/accounting")
@CrossOrigin("*")
public class AccountingController {

    private AccountingService accountingService;

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
    @GetMapping("/viewAll")
    public String viewAll(HttpSession session, Model model) {
        UserDto loggedInUser = (UserDto) session.getAttribute("loggedInUser");

        if (loggedInUser != null) { // 로그인 되어있는 상태
            model.addAttribute("loggedIn", true);
            model.addAttribute("userCode", loggedInUser.getUserCode());
            model.addAttribute("userName", loggedInUser.getName());
        } else { // 로그인 되어있지 않은 상태
            model.addAttribute("loggedIn", false);
            model.addAttribute("userName", "");
        }

        return "view";
    }

    @GetMapping("/getMore")
    @ResponseBody
    public Page<AccountingDto> getAccountingData(HttpSession session,
                                                 @RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "20") int size) {
        UserDto loggedInUser = (UserDto) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return Page.empty();
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("date").descending());
        return accountingService.findAllByUserCode(loggedInUser.getUserCode(), pageable);
    }
    /*
        월별 내역 보기
     */
    @GetMapping("/getMonthly")
    @ResponseBody
    public Page<AccountingDto> getAccountingByMonthly(HttpSession session,
                                                      @RequestParam int year,
                                                      @RequestParam int month,
                                                      @RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "20") int size) {
        UserDto loggedInUser = (UserDto) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return Page.empty();
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("date").descending());
        return accountingService.findByUserCodeAndMonth(loggedInUser.getUserCode(), year, month,  pageable);
    }
    /*
        삭제
     */
    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        accountingService.delete(id);

        return "redirect:/accounting/view";
    }
}
