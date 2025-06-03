package com.hohohehe.checktracker.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Getter
@ToString(callSuper = true)
@Table(name = "users")
@Entity
public class User extends AuditingFields implements UserDetails, CredentialsContainer {

    @Id
    private String userId;

    @Setter
    @Column(nullable = false)
    private String password;

    protected User() {

    }

    private User(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }

    public static User of(String userId, String password) {
        return new User(userId, password);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return Objects.equals(userId, user.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, password);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getUsername() {
        return userId;
    }

    @Override
    public void eraseCredentials() {
        this.password = null;
    }
}
