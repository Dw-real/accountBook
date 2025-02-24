package com.dw.AccountBook.presentation.controller;

import com.dw.AccountBook.application.UserService;
import com.dw.AccountBook.presentation.ApiResponse;
import com.dw.AccountBook.presentation.dto.login.FindPwdDto;
import com.dw.AccountBook.presentation.dto.login.PwdUpdateDto;
import com.dw.AccountBook.presentation.dto.login.WithDrawDto;
import com.dw.AccountBook.presentation.dto.user.UserDto;
import com.dw.AccountBook.presentation.dto.user.UserIdRequestDto;
import com.dw.AccountBook.presentation.dto.user.UserPwdRequestDto;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /*
        회원가입
     */
    @PostMapping("/create")
    public ResponseEntity<?> createAccount(@RequestBody UserDto userDto) {
        UserDto createdUser = userService.create(userDto);
        return ResponseEntity.ok(ApiResponse.success("회원가입 성공", createdUser));
    }

    /*
        ID 중복 확인
     */
    @GetMapping("/id-check")
    public ResponseEntity<?> idCheck(@RequestParam("id") String id) {
        boolean isValid = userService.checkId(id);
        String message = isValid ? "사용 가능한 아이디입니다." : "이미 사용 중인 아이디입니다.";
        return ResponseEntity.status(isValid ? HttpStatus.OK : HttpStatus.CONFLICT)
                .body(new ApiResponse<>(isValid, message, null));
    }

    /*
        ID 찾기
     */
    @PostMapping("/findId")
    public ResponseEntity<?> findId(@RequestBody UserIdRequestDto userIdRequestDto) {
        try {
            UserDto userDto = userService.findByInfo(userIdRequestDto);

            if (userDto == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.fail("해당 정보로 가입된 아이디가 존재하지 않습니다.", null));
            }

            return ResponseEntity.ok(ApiResponse.success("아이디 찾기 성공", userDto.getId()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.fail("서버 내부 오류: " + e.getMessage(), null));
        }

    }

    /*
        비밀번호 찾기
     */
    @PostMapping("/findPwd")
    public ResponseEntity<?> findPwd(@RequestBody UserPwdRequestDto userPwdRequestDto) {
        String pwd = userService.findPwd(userPwdRequestDto);

        if (pwd == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.fail("해당 아이디로 가입된 계정이 존재하지 않습니다.", null));
        }

        return ResponseEntity.ok(ApiResponse.success("비밀번호 찾기 성공", pwd));
    }

    @PatchMapping("/findPwd")
    public ResponseEntity<?> updatePwd(@RequestBody FindPwdDto findPwdDto) {
        boolean isUpdated = userService.updatePwd(findPwdDto.getId(), findPwdDto.getNewPwd());

        if (!isUpdated) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("비밀번호 변경에 실패했습니다.");
        }

        return ResponseEntity.ok(ApiResponse.success("비밀번호가 성공적으로 변경되었습니다.", null));
    }

    /*
        비밀번호 변경
     */
    @PatchMapping("/updatePwd")
    public ResponseEntity<?> updatePwd(HttpSession session, @RequestBody PwdUpdateDto pwdUpdateDto) {
        UserDto userDto = (UserDto) session.getAttribute("loggedInUser");

        boolean isUpdated = userService.updatePwd(userDto.getId(), pwdUpdateDto);
        if (!isUpdated) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.fail("현재 비밀번호를 확인해주세요.", null));
        }

        return ResponseEntity.ok(ApiResponse.success("비밀번호가 변경되었습니다", null));
    }

    /*
        회원탈퇴
     */
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(HttpSession session, @RequestBody WithDrawDto withDrawDto) {
        UserDto userDto = (UserDto) session.getAttribute("loggedInUser");

        if (userDto == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.fail("탈퇴 처리에 오류가 생겼습니다", null));
        }

        // 회원 정보를 제대로 입력하지 않은 경우
        if (!userService.checkInfo(userDto.getUserCode(), withDrawDto)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.fail("회원 정보가 일치하지 않습니다.", null));
        } else {
            userService.deleteUser(userDto.getUserCode());
            session.invalidate();
            return ResponseEntity.status(HttpStatus.OK)
                    .body(ApiResponse.success("탈퇴 성공", null));
        }
    }
}
