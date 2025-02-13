package com.dw.AccountBook.infrastructure;

import com.dw.AccountBook.domain.Accounting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountingRepository extends JpaRepository<Accounting, Long> {
    Page<Accounting> findByUser_UserCode(Long userCode, Pageable pageable);
}
