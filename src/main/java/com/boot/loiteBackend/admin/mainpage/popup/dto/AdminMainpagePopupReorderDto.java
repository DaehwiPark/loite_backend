package com.boot.loiteBackend.admin.mainpage.popup.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public class AdminMainpagePopupReorderDto {
    @NotEmpty
    private List<@NotNull @Positive Long> orderedIds;
}
