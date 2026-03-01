package com.hohohehe.tasktracker.model.dto;

import com.hohohehe.tasktracker.model.entity.Users;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long userSeq;
    private String userId;
    private String userName;
    private String avatarImg;
    private List<Long> groupSeqs;

    public static UserProfile of(Users users, List<Long> groups) {
        return UserProfile.builder()
                .userSeq(users.getUserSeq())
                .userId(users.getUserId())
                .userName(users.getUsername())
                .avatarImg(users.getAvatarImg())
                .groupSeqs(groups)
                .build();
    }
}
