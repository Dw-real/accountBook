package com.dw.AccountBook.presentation.controller;

import com.dw.AccountBook.presentation.dto.user.UserDto;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

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
}
