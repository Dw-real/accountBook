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

import java.util.List;
import java.util.Optional;

@Service
public class AccountingService {

    private final UserRepository userRepository;
    private final AccountingRepository accountingRepository;

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
        return accountingRepository.findByUser_UserCode(userCode, pageable)
                .map(AccountingDto::toDto);
    }

    public Page<AccountingDto> findByUserCodeAndMonth(Long userCode, int year, int month, Pageable pageable) {
        return accountingRepository.findByUserCodeAndMonth(userCode, year, month, pageable)
                .map(AccountingDto::toDto);
    }
    
    public List<AccountingDto> findByUserCodeAndMonth(Long userCode, int year, int month) {
        return accountingRepository.findByUserCodeAndMonth(userCode, year, month)
                .stream()
                .map(AccountingDto::toDto)
                .toList();
    }

    @Transactional
    public AccountingDto findById(Long id) {
        Optional<Accounting> optionalAccounting = accountingRepository.findById(id);
        if (optionalAccounting.isPresent()) {
            Accounting accounting = optionalAccounting.get();
            return AccountingDto.toDto(accounting);
        } else {
            return null;
        }
    }

    @Transactional
    public AccountingDto update(AccountingDto accountingDto) {
        Accounting existAccounting = accountingRepository.findById(accountingDto.getId())
                .orElseThrow(() -> new RuntimeException("해당 게시글이 존재하지 않습니다."));

        User existUser = userRepository.findById(accountingDto.getUserCode())
                .orElseThrow(() -> new RuntimeException("해당 사용자가 존재하지 않습니다."));

        Accounting.updateEntity(existAccounting, accountingDto, existUser);

        return AccountingDto.toDto(existAccounting);
    }

    @Transactional
    public void delete(Long id) {
        accountingRepository.deleteById(id);
    }
}
