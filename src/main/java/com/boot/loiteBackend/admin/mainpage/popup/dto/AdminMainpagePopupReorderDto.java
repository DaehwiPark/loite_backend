package com.boot.loiteBackend.admin.mainpage.popup.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminMainpagePopupReorderDto {
    @NotEmpty
    private List<@NotNull @Positive Long> orderedIds;
}
