package com.dw.AccountBook.infrastructure;

import com.dw.AccountBook.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = "SELECT u.userCode FROM User u WHERE u.id = :id")
    Optional<Long> checkId(String id); // id 존재 여부 반환

    @Query(value = "SELECT u FROM User u WHERE u.name = :name AND u.email = :email")
    Optional<User> findByInfo(String name, String email);

    @Query(value = "SELECT u FROM User u WHERE u.userCode = :userCode")
    User findByUserCode(Long userCode);

    @Query(value = "SELECT u FROM User u WHERE u.id = :id")
    User findUserById(String id);

    @Query(value = "SELECT u.pwd FROM User u WHERE u.id = :id AND u.email = :email")
    Optional<String> findPwd(String id, String email);

    @Modifying
    @Query(value = "UPDATE User u SET u.pwd = :pwd WHERE u.id = :id")
    void updatePwd(String id, String pwd);

    void deleteByUserCode(Long userCode);
}
