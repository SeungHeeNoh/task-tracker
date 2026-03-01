package com.hohohehe.tasktracker.model.dto;

import com.hohohehe.tasktracker.model.entity.Groups;
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
    private List<Groups> group;

    public static UserProfile of(Users users) {
        return UserProfile.builder()
                .userSeq(users.getUserSeq())
                .userId(users.getUserId())
                .userName(users.getUsername())
                .avatarImg(users.getAvatarImg())
                .group(users.getGroup())
                .build();
    }
}
