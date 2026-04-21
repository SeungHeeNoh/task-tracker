package com.hohohehe.tasktracker.model.dto.request;

import org.springframework.util.StringUtils;

public record JoinRequest(
        String userId,
        String userName,
        String password,
        String avatarImg
) {

    public void checkValidation() {
        // 1. ID (아이디) 검증
        if (userId == null || !userId.matches("^[a-zA-Z0-9]{4,50}$")) {
            throw new IllegalArgumentException("ID는 4~50자의 영문 대소문자 및 숫자만 가능합니다.");
        }

        // 2. Nickname (닉네임) 검증
        if (!StringUtils.hasText(userName) || userName.trim().length() < 1 || userName.trim().length() > 50) {
            throw new IllegalArgumentException("닉네임은 1~50자 사이로 입력해주세요.");
        }

        // 3. Password (비밀번호) 검증
        if (password == null || !password.matches("(?=.*[a-z])(?=.*[A-Z])(?=.*[\\W_]).{6,}")) {
            throw new IllegalArgumentException("비밀번호는 6자 이상이며, 대문자, 소문자, 특수문자를 각각 최소 1개 이상 포함해야 합니다.");
        }

        // 4. Avatar Image (프로필 사진) 검증 - 선택 사항
        if (StringUtils.hasLength(avatarImg)) {
            // Base64 문자열인 경우 실제 바이트 크기 계산 (대략적인 계산: length * 0.75)
            long sizeInBytes = (long) (avatarImg.length() * 0.75);
            if (sizeInBytes > 2 * 1024 * 1024) {
                throw new IllegalArgumentException("아바타 이미지는 2MB 이하여야 합니다.");
            }
        }
    }
}
