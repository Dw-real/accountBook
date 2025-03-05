package com.dw.AccountBook.presentation.controller;

import com.dw.AccountBook.application.AccountingService;
import com.dw.AccountBook.presentation.dto.accounting.AccountingDto;
import com.dw.AccountBook.presentation.dto.user.UserDto;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.io.PrintWriter;

@Controller
public class HomeController {

    private final AccountingService accountingService;

    @Autowired
    public HomeController(AccountingService accountingService) {
        this.accountingService = accountingService;
    }

    @GetMapping("/")
    public String home(HttpSession session, Model model) {
        UserDto loggedInUser = (UserDto)session.getAttribute("loggedInUser");

        if (loggedInUser != null) { // 로그인 되어있는 상태
            model.addAttribute("loggedIn", true);
            model.addAttribute("userName", loggedInUser.getName());
        } else { // 로그인 되어있지 않은 상태
            model.addAttribute("loggedIn", false);
            model.addAttribute("userName", "");
        }

        return "home";
    }

    @GetMapping("/logIn")
    public String logIn() {
        return "logIn";
    }

    @GetMapping("/create")
    public String createForm() {
        return "create";
    }

    @GetMapping("/findId")
    public String findIdForm() {
        return "findId";
    }

    @GetMapping("/findPwd")
    public String findPwdForm() {
        return "findPwd";
    }

    @GetMapping("/updatePwd")
    public String updateForm() {
        return "updatePwd";
    }

    @GetMapping("/post")
    public String registerForm(HttpSession session, HttpServletResponse response, Model model) throws IOException {
        setUserSessionAttributes(session, response, model);
        return "post";
    }

    @GetMapping("/update/{id}")
    public String updateForm(@PathVariable Long id, HttpSession session, HttpServletResponse response,
                             Model model) throws IOException {
        AccountingDto accountingDto = accountingService.findById(id);
        setUserSessionAttributes(session, response, model);
        model.addAttribute("accounting", accountingDto);

        return "update";
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

    @GetMapping("/viewAll")
    public String viewAll(HttpSession session, HttpServletResponse response, Model model) throws IOException {
        setUserSessionAttributes(session, response, model);
        return "view";
    }

    @GetMapping("/viewMonthly")
    public String viewMonthly(HttpSession session, HttpServletResponse response, Model model) throws IOException {
        setUserSessionAttributes(session, response, model);
        return "viewMonthly";
    }

    @GetMapping("/analysisMonthly")
    public String analysisMonthly(HttpSession session, HttpServletResponse response, Model model) throws IOException {
        setUserSessionAttributes(session, response, model);
        return "analysis";
    }

    @GetMapping("/budget")
    public String viewBudget(HttpSession session, HttpServletResponse response, Model model) throws IOException {
        setUserSessionAttributes(session, response, model);
        return "budget";
    }

    @GetMapping("/withDraw")
    public String withDraw() {
        return "withDraw";
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
