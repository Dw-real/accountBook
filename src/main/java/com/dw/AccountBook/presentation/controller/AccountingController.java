package com.dw.AccountBook.presentation.controller;

import com.dw.AccountBook.application.AccountingService;
import com.dw.AccountBook.presentation.ApiResponse;
import com.dw.AccountBook.presentation.dto.accounting.AccountingDto;
import com.dw.AccountBook.presentation.dto.user.UserDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/accounting")
@CrossOrigin("*")
public class AccountingController {

    private AccountingService accountingService;

    @Autowired
    public AccountingController(AccountingService accountingService) {
        this.accountingService = accountingService;
    }

    @GetMapping("/post")
    public String registerForm(HttpSession session, HttpServletResponse response, Model model) throws IOException {
        setUserSessionAttributes(session, response, model);
        return "post";
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
    public String viewAll(HttpSession session, HttpServletResponse response, Model model) throws IOException {
        setUserSessionAttributes(session, response, model);
        return "view";
    }

    @GetMapping("/getMore")
    @ResponseBody
    public ResponseEntity<Page<AccountingDto>> getAccountingData(HttpSession session,
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
    @GetMapping("/viewMonthly")
    public String viewMonthly(HttpSession session, HttpServletResponse response, Model model) throws IOException {
        setUserSessionAttributes(session, response, model);
        return "viewMonthly";
    }

    @GetMapping("/getMonthly")
    @ResponseBody
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
    @GetMapping("/analysisMonthly")
    public String analysisMonthly(HttpSession session, HttpServletResponse response, Model model) throws IOException {
        setUserSessionAttributes(session, response, model);
        return "analysis";
    }

    @GetMapping("/getMonthlyData")
    @ResponseBody
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
        조회
     */
    @GetMapping("/lookUp/{id}")
    public String lookUp(@PathVariable Long id, HttpSession session, HttpServletResponse response,
                         Model model) throws IOException {
        setUserSessionAttributes(session, response, model);
        AccountingDto accountingDto = accountingService.findById(id);

        model.addAttribute("accounting", accountingDto);

        return "detail";
    }

    /*
        수정
     */
    @GetMapping("/update/{id}")
    public String updateForm(@PathVariable Long id, Model model) {
        AccountingDto accountingDto = accountingService.findById(id);

        model.addAttribute("accounting", accountingDto);

        return "update";
    }

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

    private void setUserSessionAttributes(HttpSession session, HttpServletResponse response, Model model) throws IOException {
        UserDto loggedInUser = (UserDto) session.getAttribute("loggedInUser");

        if (loggedInUser != null) { // 로그인 되어있는 상태
            model.addAttribute("loggedIn", true);
            model.addAttribute("userCode", loggedInUser.getUserCode());
            model.addAttribute("userName", loggedInUser.getName());
        } else { // 로그인 되어있지 않은 상태
            model.addAttribute("loggedIn", false);
            model.addAttribute("userName", "");
            showAlert(response);
        }
    }

    private void showAlert(HttpServletResponse response) throws IOException {
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<script>alert('로그인이 필요합니다'); location.href='/logIn'</script>");
        out.flush();
    }
}
