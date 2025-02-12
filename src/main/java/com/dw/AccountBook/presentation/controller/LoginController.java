package com.dw.AccountBook.presentation.controller;

import com.dw.AccountBook.application.UserService;
import com.dw.AccountBook.presentation.ApiResponse;
import com.dw.AccountBook.presentation.dto.login.LoginDto;
import com.dw.AccountBook.presentation.dto.user.UserDto;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginController {

    private UserService userService;

    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/logIn")
    public String logIn() {
        return "logIn";
    }

    @PostMapping("/loginTry")
    @ResponseBody
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto, HttpSession session) {
        boolean isAuthenticated = userService.logIn(loginDto.getId(), loginDto.getPwd());

        if (isAuthenticated) {
            UserDto userDto = userService.findById(loginDto.getId());
            session.setAttribute("loggedInUser", userDto);
            return ResponseEntity.ok("redirect:/");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.fail("아이디, 비밀번호를 확인해주세요.", null));
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("loggedInUser");
        session.invalidate();
        return "redirect:/";
    }
}
