package com.dw.AccountBook.infrastructure;

import com.dw.AccountBook.domain.Accounting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountingRepository extends JpaRepository<Accounting, Long> {
    Page<Accounting> findByUser_UserCode(Long userCode, Pageable pageable);

    @Query("SELECT a FROM Accounting a WHERE a.user.userCode = :userCode AND YEAR(a.date) = :year AND MONTH(a.date) = :month")
    Page<Accounting> findByUserCodeAndMonth(Long userCode, int year, int month, Pageable pageable);

    @Query("SELECT a FROM Accounting a WHERE a.user.userCode = :userCode AND YEAR(a.date) = :year AND MONTH(a.date) = :month")
    List<Accounting> findByUserCodeAndMonth(Long userCode, int year, int month);
}
