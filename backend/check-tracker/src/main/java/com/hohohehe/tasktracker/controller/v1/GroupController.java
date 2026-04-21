package com.hohohehe.tasktracker.controller.v1;

import com.hohohehe.tasktracker.common.enumCode.ErrorCode;
import com.hohohehe.tasktracker.common.exception.SystemException;
import com.hohohehe.tasktracker.common.response.CommonResponse;
import com.hohohehe.tasktracker.model.dto.InvitationCodeResponse;
import com.hohohehe.tasktracker.model.dto.request.IssueInvitationRequest;
import com.hohohehe.tasktracker.service.InvitationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/groups")
@RestController
public class GroupController {

    private final InvitationService invitationService;

    @PostMapping("/{groupSeq}/invitations")
    public CommonResponse<InvitationCodeResponse> issueInvitation(
            @PathVariable Long groupSeq,
            @RequestBody(required = false) IssueInvitationRequest request
    ) {
        try {
            Integer maxUses = (request == null) ? null : request.maxUses();
            InvitationCodeResponse data = invitationService.issueInvitation(groupSeq, maxUses);
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
}
