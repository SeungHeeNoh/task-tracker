package com.hohohehe.tasktracker.controller.v1;

import com.hohohehe.tasktracker.common.SecurityContext;
import com.hohohehe.tasktracker.common.response.CommonResponse;
import com.hohohehe.tasktracker.model.dto.request.ModifyUserRequest;
import com.hohohehe.tasktracker.model.dto.request.PasswordChangeRequest;
import com.hohohehe.tasktracker.model.entity.Users;
import com.hohohehe.tasktracker.service.UsersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@RestController
public class UsersController {

    private final UsersService usersService;

    @PostMapping("/{userSeq}/modify")
    public CommonResponse<Void> modifyUser(@PathVariable Long userSeq, @RequestBody ModifyUserRequest modifyUserRequest) {
        try {
            if (!Objects.equals(SecurityContext.getCurrentUser().getUserSeq(), userSeq)) {
                return CommonResponse.fail("본인 정보만 수정 가능합니다.");
            }

            modifyUserRequest.checkValidation();

            usersService.modifyUser(Users.of(userSeq, modifyUserRequest));
            return CommonResponse.success("정보 수정을 완료했습니다.");
        } catch (Exception e) {
            return CommonResponse.fail(e.getMessage());
        }
    }


    @PostMapping("/{userSeq}/password")
    public CommonResponse<Void> changePassword(@PathVariable Long userSeq, @RequestBody PasswordChangeRequest passwordChangeRequest) {
        try {

            passwordChangeRequest.checkValidation();

            if (!Objects.equals(SecurityContext.getCurrentUser().getUserSeq(), userSeq)) {
                return CommonResponse.fail("본인 정보만 수정 가능합니다.");
            }

            usersService.changePassword(passwordChangeRequest);
            return CommonResponse.success("비밀번호가 변경되었습니다.");
        } catch (IllegalArgumentException e) {
            return CommonResponse.fail(e.getMessage());
        } catch (Exception e) {
            return CommonResponse.fail("비밀번호를 변경하던 중 오류가 발생했습니다.");
        }
    }
}
