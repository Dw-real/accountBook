package com.dw.AccountBook.presentation.controller;

import com.dw.AccountBook.application.AccountingService;
import com.dw.AccountBook.domain.Accounting;
import com.dw.AccountBook.presentation.ApiResponse;
import com.dw.AccountBook.presentation.dto.accounting.AccountingDto;
import com.dw.AccountBook.presentation.dto.user.UserDto;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
    public String viewAll(HttpSession session, @PageableDefault(page = 0) Pageable pageable, Model model) {
        UserDto loggedInUser = (UserDto) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return "redirect:/login";
        }

        Page<AccountingDto> accountingList = accountingService.findAllByUserCode(loggedInUser.getUserCode(), pageable);

        int blockLimit = 10;
        int startPage = (((int)(Math.ceil((double)(pageable.getPageNumber() + 1) / blockLimit))) - 1) * blockLimit + 1;
        int endPage = (accountingList.getTotalPages() == 0) ? 1 : Math.min((startPage + blockLimit - 1), accountingList.getTotalPages());

        model.addAttribute("accountingList", accountingList);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "view";
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
