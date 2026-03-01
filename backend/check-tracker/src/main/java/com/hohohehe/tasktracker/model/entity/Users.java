package com.hohohehe.tasktracker.model.entity;

import com.hohohehe.tasktracker.model.dto.UserProfile;
import com.hohohehe.tasktracker.model.dto.request.JoinRequest;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class Users implements UserDetails {
    private Long userSeq;
    private String userId;
    private String userName;
    private String password;
    private String avatarImg;
    private LocalDateTime createdAt;
    private Long createdBy;
    private LocalDateTime modifiedAt;
    private Long modifiedBy;

    private List<Groups> group = new ArrayList<>();

    public static Users from(JoinRequest joinRequest) {
        return Users.builder()
                .userId(joinRequest.userId())
                .userName(joinRequest.userName())
                .password(joinRequest.password())
                .avatarImg(joinRequest.avatarImg())
                .build();
    }

    public static Users from(UserProfile userProfile) {
        return Users.builder()
                .userId(userProfile.getUserId())
                .userName(userProfile.getUserName())
                .avatarImg(userProfile.getAvatarImg())
                .group(userProfile.getGroup())
                .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
