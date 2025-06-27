package com.boot.loiteBackend.global.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Getter
@AllArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final Long userId;
    private final String role;
    private final String username; // 변경: userEmail → username

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList(); // 권한 리스트 필요 시 여기에 매핑
    }

    @Override
    public String getPassword() {
        return null; // 인증 방식이 JWT이므로 비밀번호는 필요 없음
    }

    @Override
    public String getUsername() {
        return username; // 변경된 필드 반영
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
