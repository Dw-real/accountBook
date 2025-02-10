package com.dw.AccountBook.infrastructure;

import com.dw.AccountBook.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRespository extends JpaRepository<User, Long> {

}
