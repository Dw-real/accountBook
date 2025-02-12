package com.dw.AccountBook.application;

import com.dw.AccountBook.domain.User;
import com.dw.AccountBook.infrastructure.UserRepository;
import com.dw.AccountBook.presentation.dto.login.PwdUpdateDto;
import com.dw.AccountBook.presentation.dto.login.WithDrawDto;
import com.dw.AccountBook.presentation.dto.user.UserDto;
import com.dw.AccountBook.presentation.dto.user.UserIdRequestDto;
import com.dw.AccountBook.presentation.dto.user.UserPwdRequestDto;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public UserDto create(UserDto userDto) {
        User user = UserDto.toEntity(userDto);
        String encodedPwd = bCryptPasswordEncoder.encode(userDto.getPwd());
        user.setPwd(encodedPwd);
        userRepository.save(user);

        return UserDto.toDto(user);
    }

    public boolean checkId(String id) {
        // 해당 id를 사용하는 사용자의 userCode
        Long userCode = userRepository.checkId(id)
                .orElse(null);

        return userCode == null;
    }

    public boolean logIn(String userId, String userPwd) {
        User user = userRepository.findUserById(userId);
        return user != null && bCryptPasswordEncoder.matches(userPwd, user.getPwd());
    }

    public UserDto findById(String id) {
        User user = userRepository.findUserById(id);

        return UserDto.toDto(user);
    }

    @Transactional(readOnly = true)
    public UserDto findByInfo(UserIdRequestDto userIdRequestDto) {
        return userRepository.findByInfo(userIdRequestDto.getName(), userIdRequestDto.getEmail())
                .map(UserDto::toDto)
                .orElse(null);
    }

    public String findPwd(UserPwdRequestDto userPwdRequestDto) {
        return userRepository.findPwd(userPwdRequestDto.getId(), userPwdRequestDto.getEmail())
                .orElse(null);
    }

    @Transactional
    public boolean updatePwd(String id, String newPwd) {
        // 새 비밀번호로 업데이트
        String encodedNewPwd = bCryptPasswordEncoder.encode(newPwd);  // 새 비밀번호 암호화
        userRepository.updatePwd(id, encodedNewPwd);  // 암호화된 새 비밀번호로 업데이트
        return true;
    }

    @Transactional
    public boolean updatePwd(String id, PwdUpdateDto pwdUpdateDto) {
        User user = userRepository.findUserById(id);

        // 현재 비밀번호가 일치하는지 확인
        if (!bCryptPasswordEncoder.matches(pwdUpdateDto.getCurrentPwd(), user.getPwd())) {
            return false;  // 비밀번호가 일치하지 않으면 false 반환
        }

        // 새 비밀번호로 업데이트
        String encodedNewPwd = bCryptPasswordEncoder.encode(pwdUpdateDto.getNewPwd());  // 새 비밀번호 암호화
        userRepository.updatePwd(id, encodedNewPwd);  // 암호화된 새 비밀번호로 업데이트
        return true;
    }

    public boolean checkInfo(Long userCode, WithDrawDto withDrawDto) {
        User user = userRepository.findByUserCode(userCode);
        System.out.println(user.getEmail().equals(withDrawDto.getEmail()) && bCryptPasswordEncoder.matches(withDrawDto.getPwd(), user.getPwd()));
        return user.getEmail().equals(withDrawDto.getEmail()) && bCryptPasswordEncoder.matches(withDrawDto.getPwd(), user.getPwd());
    }

    @Transactional
    public void deleteUser(Long userCode) {
        userRepository.deleteByUserCode(userCode);
    }
}
