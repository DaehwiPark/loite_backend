package com.boot.loiteBackend.web.user.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserSignedUpEvent {
    private final Long userId;
}