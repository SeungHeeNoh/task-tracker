package com.hohohehe.tasktracker.service;

import com.hohohehe.tasktracker.common.SecurityContext;
import com.hohohehe.tasktracker.common.enumCode.ErrorCode;
import com.hohohehe.tasktracker.common.enumCode.GroupRole;
import com.hohohehe.tasktracker.common.exception.SystemException;
import com.hohohehe.tasktracker.config.redis.RedisProperties;
import com.hohohehe.tasktracker.mapper.GroupsMapper;
import com.hohohehe.tasktracker.mapper.UserGroupMapMapper;
import com.hohohehe.tasktracker.model.dto.InvitationCodeResponse;
import com.hohohehe.tasktracker.model.dto.InvitationPreview;
import com.hohohehe.tasktracker.model.entity.Groups;
import com.hohohehe.tasktracker.model.entity.UserGroupMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.Map;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class InvitationService {

    private static final int GROUP_MEMBER_LIMIT = 10;
    private static final int USER_GROUP_LIMIT = 5;
    private static final int DEFAULT_MAX_USES = 10;
    private static final int MAX_USES_UPPER_BOUND = 100;
    private static final int CODE_LENGTH = 8;
    // 0/O, 1/I 등 혼동 문자 제외
    private static final char[] CODE_ALPHABET = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789".toCharArray();

    private final GroupsMapper groupsMapper;
    private final UserGroupMapMapper userGroupMapMapper;
    private final RedisService redisService;
    private final RedisProperties redisProperties;
    private final SecureRandom secureRandom = new SecureRandom();

    public InvitationCodeResponse issueInvitation(Long groupSeq, Integer requestedMaxUses) {
        try {
            Long currentUserSeq = SecurityContext.getCurrentUser().getUserSeq();

            Groups group = groupsMapper.findBySeq(groupSeq);
            if (group == null) {
                throw new SystemException(ErrorCode.GROUP_NOT_FOUND);
            }

            GroupRole role = userGroupMapMapper.findRoleByUserAndGroup(groupSeq, currentUserSeq);
            if (role != GroupRole.OWNER) {
                throw new SystemException(ErrorCode.GROUP_ACCESS_DENIED);
            }

            if (userGroupMapMapper.countByGroupSeq(groupSeq) >= GROUP_MEMBER_LIMIT) {
                throw new SystemException(ErrorCode.GROUP_MEMBER_LIMIT_EXCEEDED);
            }

            int maxUses = resolveMaxUses(requestedMaxUses);
            String code = generateCode();
            long ttl = redisProperties.getTtl().getInvitationTtl();

            redisService.saveInvitationCode(code, groupSeq, currentUserSeq, maxUses);

            return InvitationCodeResponse.builder()
                    .code(code)
                    .maxUses(maxUses)
                    .expiresInSeconds(ttl)
                    .build();
        } catch (SystemException e) {
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new SystemException(e, "초대 코드 발급 중 오류가 발생했습니다.");
        }
    }

    @Transactional(readOnly = true)
    public InvitationPreview getInvitationPreview(String code) {
        try {
            Map<String, Object> meta = redisService.findInvitationCode(code);
            if (meta == null) {
                throw new SystemException(ErrorCode.INVITATION_NOT_FOUND);
            }

            Long groupSeq = toLong(meta.get("groupSeq"));
            Groups group = groupsMapper.findBySeq(groupSeq);
            if (group == null) {
                throw new SystemException(ErrorCode.GROUP_NOT_FOUND);
            }

            int memberCount = userGroupMapMapper.countByGroupSeq(groupSeq);
            return toPreview(groupSeq, group.getGroupName(), memberCount);
        } catch (SystemException e) {
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new SystemException(e, "초대 코드 조회 중 오류가 발생했습니다.");
        }
    }

    public InvitationPreview acceptInvitation(String code) {
        try {
            Long currentUserSeq = SecurityContext.getCurrentUser().getUserSeq();

            Map<String, Object> meta = redisService.findInvitationCode(code);
            if (meta == null) {
                throw new SystemException(ErrorCode.INVITATION_NOT_FOUND);
            }

            Long groupSeq = toLong(meta.get("groupSeq"));
            Groups group = groupsMapper.findBySeq(groupSeq);
            if (group == null) {
                throw new SystemException(ErrorCode.GROUP_NOT_FOUND);
            }

            // 이미 멤버인 경우 멱등 처리
            if (userGroupMapMapper.existsMember(groupSeq, currentUserSeq)) {
                return toPreview(groupSeq, group.getGroupName(), userGroupMapMapper.countByGroupSeq(groupSeq));
            }

            if (userGroupMapMapper.countByGroupSeq(groupSeq) >= GROUP_MEMBER_LIMIT) {
                throw new SystemException(ErrorCode.GROUP_MEMBER_LIMIT_EXCEEDED);
            }

            if (userGroupMapMapper.countByUserSeq(currentUserSeq) >= USER_GROUP_LIMIT) {
                throw new SystemException(ErrorCode.USER_GROUP_LIMIT_EXCEEDED);
            }

            Long remaining = redisService.decrementInvitationUses(code);
            if (remaining == null || remaining < 0) {
                redisService.deleteInvitationCode(code);
                throw new SystemException(ErrorCode.INVITATION_EXHAUSTED);
            }

            UserGroupMap userGroupMap = new UserGroupMap();
            userGroupMap.setGroupSeq(groupSeq);
            userGroupMap.setUserSeq(currentUserSeq);
            userGroupMap.setRole(GroupRole.MEMBER);
            userGroupMap.setCreatedBy(currentUserSeq);
            userGroupMapMapper.insertMember(userGroupMap);

            if (remaining == 0) {
                redisService.deleteInvitationCode(code);
            }

            return toPreview(groupSeq, group.getGroupName(), userGroupMapMapper.countByGroupSeq(groupSeq));
        } catch (SystemException e) {
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new SystemException(e, "초대 수락 중 오류가 발생했습니다.");
        }
    }

    private int resolveMaxUses(Integer requested) {
        if (requested == null || requested <= 0) {
            return DEFAULT_MAX_USES;
        }
        return Math.min(requested, MAX_USES_UPPER_BOUND);
    }

    private String generateCode() {
        char[] buf = new char[CODE_LENGTH];
        for (int i = 0; i < CODE_LENGTH; i++) {
            buf[i] = CODE_ALPHABET[secureRandom.nextInt(CODE_ALPHABET.length)];
        }
        return new String(buf);
    }

    // Jackson 역직렬화가 작은 값을 Integer로 복원할 수 있어 Number → long 변환 보정
    private Long toLong(Object o) {
        if (o == null) {
            return null;
        }
        if (o instanceof Number n) {
            return n.longValue();
        }
        return Long.parseLong(o.toString());
    }

    private InvitationPreview toPreview(Long groupSeq, String groupName, int memberCount) {
        return InvitationPreview.builder()
                .groupSeq(groupSeq)
                .groupName(groupName)
                .memberCount(memberCount)
                .build();
    }
}
