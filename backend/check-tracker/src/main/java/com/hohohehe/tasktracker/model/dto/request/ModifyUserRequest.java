package com.hohohehe.tasktracker.model.dto.request;

import com.hohohehe.tasktracker.common.SecurityContext;
import org.springframework.util.StringUtils;

public record ModifyUserRequest(
        String userName,
        String avatarImg
) {

    public void checkValidation() {
        // 1. Nickname (닉네임) 검증
        if (!StringUtils.hasText(userName) || userName.trim().length() < 1 || userName.trim().length() > 50) {
            throw new IllegalArgumentException("닉네임은 1~50자 사이로 입력해주세요.");
        }

        // 2. Avatar Image (프로필 사진) 검증 - 선택 사항
        if (StringUtils.hasLength(avatarImg)) {
            // Base64 문자열인 경우 실제 바이트 크기 계산 (대략적인 계산: length * 0.75)
            long sizeInBytes = (long) (avatarImg.length() * 0.75);
            if (sizeInBytes > 2 * 1024 * 1024) {
                throw new IllegalArgumentException("아바타 이미지는 2MB 이하여야 합니다.");
            }
        }
    }
}
