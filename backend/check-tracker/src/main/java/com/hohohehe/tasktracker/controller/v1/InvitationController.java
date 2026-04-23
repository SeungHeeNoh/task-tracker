package com.hohohehe.tasktracker.controller.v1;

import com.hohohehe.tasktracker.common.enumCode.ErrorCode;
import com.hohohehe.tasktracker.common.exception.SystemException;
import com.hohohehe.tasktracker.common.response.CommonResponse;
import com.hohohehe.tasktracker.model.dto.InvitationCodeResponse;
import com.hohohehe.tasktracker.model.dto.InvitationPreview;
import com.hohohehe.tasktracker.model.dto.request.IssueInvitationRequest;
import com.hohohehe.tasktracker.service.InvitationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/invitations")
@RestController
public class InvitationController {

    private final InvitationService invitationService;

    @PostMapping
    public CommonResponse<InvitationCodeResponse> issue(@RequestBody IssueInvitationRequest request) {
        try {
            InvitationCodeResponse data = invitationService.issueInvitation(request.groupSeq(), request.maxUses());
            return CommonResponse.success("초대 코드가 발급되었습니다.", data);
        } catch (SystemException e) {
            if (e.getErrorCode() != null) {
                return CommonResponse.fail(e.getErrorCode());
            }
            return CommonResponse.fail(ErrorCode.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return CommonResponse.fail(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{code}")
    public CommonResponse<InvitationPreview> preview(@PathVariable(value = "code") String code) {
        try {
            InvitationPreview data = invitationService.getInvitationPreview(code);
            return CommonResponse.success("초대 코드 정보를 조회했습니다.", data);
        } catch (SystemException e) {
            if (e.getErrorCode() != null) {
                return CommonResponse.fail(e.getErrorCode());
            }
            return CommonResponse.fail(ErrorCode.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return CommonResponse.fail(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{code}/accept")
    public CommonResponse<InvitationPreview> accept(@PathVariable(value = "code") String code) {
        try {
            InvitationPreview data = invitationService.acceptInvitation(code);
            return CommonResponse.success("그룹에 가입되었습니다.", data);
        } catch (SystemException e) {
            if (e.getErrorCode() != null) {
                return CommonResponse.fail(e.getErrorCode());
            }
            return CommonResponse.fail(ErrorCode.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return CommonResponse.fail(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}
