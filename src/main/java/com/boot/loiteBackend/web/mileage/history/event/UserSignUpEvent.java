package com.boot.loiteBackend.web.mileage.history.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserSignUpEvent {

    private final Long userId;

}
