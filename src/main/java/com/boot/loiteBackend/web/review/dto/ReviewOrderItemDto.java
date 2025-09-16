package com.boot.loiteBackend.web.review.dto;

import com.boot.loiteBackend.web.order.dto.OrderAdditionalResponseDto;
import com.boot.loiteBackend.web.order.dto.OrderGiftResponseDto;
import com.boot.loiteBackend.web.order.dto.OrderOptionResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Schema
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewOrderItemDto {
    @Schema(description = "주문 아이템 ID")
    private Long orderItemId;

    @Schema(description = "옵션 목록")
    private List<OrderOptionResponseDto> options;

    @Schema(description = "추가구성품 목록")
    private List<OrderAdditionalResponseDto> additionals;

    @Schema(description = "사은품 목록")
    private List<OrderGiftResponseDto> gifts;
}
