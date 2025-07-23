package com.boot.loiteBackend.web.cartItem.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

@Getter
public class CartItemDeleteRequestDto {

    @Schema(description = "삭제할 장바구니 항목 ID 목록", example = "[101, 102, 103]")
    private List<Long> cartItemIds;
}