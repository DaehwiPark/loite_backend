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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 권한 부여는 여기서 필요 시 처리
        return Collections.emptyList(); // 또는 ROLE 기반 Authority 생성
    }

    @Override public String getPassword() { return null; }
    @Override public String getUsername() { return String.valueOf(userId); }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}
