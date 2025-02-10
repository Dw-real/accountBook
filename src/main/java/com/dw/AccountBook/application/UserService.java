package com.dw.AccountBook.application;

import com.dw.AccountBook.domain.User;
import com.dw.AccountBook.infrastructure.UserRespository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRespository userRespository;

    public UserService(UserRespository userRespository) {
        this.userRespository = userRespository;
    }

    public void create(User user) {
        userRespository.save(user);
    }
}
