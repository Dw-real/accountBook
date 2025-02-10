package com.dw.AccountBook.presentation;

import com.dw.AccountBook.application.UserService;
import com.dw.AccountBook.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
@CrossOrigin(origins = "*")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createAccount(@RequestBody User user) {
        userService.create(user);
        return ResponseEntity.ok(user);
    }
}
