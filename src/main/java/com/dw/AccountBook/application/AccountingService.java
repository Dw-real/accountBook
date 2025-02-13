package com.dw.AccountBook.application;

import com.dw.AccountBook.domain.Accounting;
import com.dw.AccountBook.domain.User;
import com.dw.AccountBook.infrastructure.AccountingRepository;
import com.dw.AccountBook.infrastructure.UserRepository;
import com.dw.AccountBook.presentation.dto.accounting.AccountingDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AccountingService {

    private UserRepository userRepository;
    private AccountingRepository accountingRepository;

    @Autowired
    public AccountingService(UserRepository userRepository, AccountingRepository accountingRepository) {
        this.userRepository = userRepository;
        this.accountingRepository = accountingRepository;
    }

    public void register(AccountingDto accountingDto) {
        Optional<User> user = userRepository.findById(accountingDto.getUserCode());

        if (user.isPresent()) {
            Accounting accounting = Accounting.toEntity(accountingDto, user.orElse(null));
            accountingRepository.save(accounting);
        }
    }

    public Page<AccountingDto> findAllByUserCode(Long userCode, Pageable pageable) {
        Page<Accounting> accountingPage = accountingRepository.findByUser_UserCode(userCode, pageable);
        System.out.println("Total elements: " + accountingPage.getTotalElements());  // 전체 데이터 개수
        System.out.println("Total pages: " + accountingPage.getTotalPages());  // 전체 페이지 수
        System.out.println("Content size: " + accountingPage.getContent().size());  // 현재 페이지의 데이터 개수
        return accountingRepository.findByUser_UserCode(userCode, pageable)
                .map(AccountingDto::toDto);
    }

    @Transactional
    public void delete(Long id) {
        accountingRepository.deleteById(id);
    }
}
