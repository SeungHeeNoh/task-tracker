package com.hohohehe.tasktracker.controller.v1;

import com.hohohehe.tasktracker.common.SecurityContext;
import com.hohohehe.tasktracker.common.enumCode.ErrorCode;
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
                return CommonResponse.fail(ErrorCode.USER_ACCESS_DENIED);
            }

            modifyUserRequest.checkValidation();

            usersService.modifyUser(Users.of(userSeq, modifyUserRequest));
            return CommonResponse.success("정보 수정을 완료했습니다.");
        } catch (IllegalArgumentException e) {
            return CommonResponse.fail(ErrorCode.INVALID_REQUEST, e.getMessage());
        } catch (Exception e) {
            return CommonResponse.fail(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/{userSeq}/password")
    public CommonResponse<Void> changePassword(@PathVariable Long userSeq, @RequestBody PasswordChangeRequest passwordChangeRequest) {
        try {
            if (!Objects.equals(SecurityContext.getCurrentUser().getUserSeq(), userSeq)) {
                return CommonResponse.fail(ErrorCode.USER_ACCESS_DENIED);
            }

            passwordChangeRequest.checkValidation();

            usersService.changePassword(passwordChangeRequest);
            return CommonResponse.success("비밀번호가 변경되었습니다.");
        } catch (IllegalArgumentException e) {
            return CommonResponse.fail(ErrorCode.INVALID_REQUEST, e.getMessage());
        } catch (Exception e) {
            return CommonResponse.fail(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}
