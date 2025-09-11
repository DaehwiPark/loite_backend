package com.boot.loiteBackend.admin.home.best.controller;

import com.boot.loiteBackend.admin.home.best.dto.AdminHomeBestItemCreateDto;
import com.boot.loiteBackend.admin.home.best.dto.AdminHomeBestItemResponseDto;
import com.boot.loiteBackend.admin.home.best.dto.AdminHomeBestItemUpdateDto;
import com.boot.loiteBackend.admin.home.best.service.AdminHomeBestItemService;
import com.boot.loiteBackend.config.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin/home/best/items")
@Tag(name = "홈 인기 아이템 관리 API", description = "상품 카드(슬롯 1~10) CRUD")
@Validated
public class AdminHomeBestItemController {

    private final AdminHomeBestItemService adminHomeBestItemService;

    @Operation(
            summary = "인기 아이템 등록",
            description = "인기 섹션에 상품을 등록합니다.<br>" +
                          "productId` 를 필수로 입력합니다.<br>" +
                          "- `slotNo` 는 1~10 범위이며, 지정 슬롯이 이미 차 있으면 자동으로 뒤 슬롯들이 밀립니다.<br>" +
                          "- 최대 10개까지 등록 가능하며, 초과 시 에러가 발생합니다."
    )
    @PostMapping
    public ResponseEntity<AdminHomeBestItemResponseDto> create(
            @RequestBody @Valid AdminHomeBestItemCreateDto CreateDto,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        Long userId = (user != null) ? user.getUserId() : null;
        return ResponseEntity.status(HttpStatus.CREATED).body(adminHomeBestItemService.create(CreateDto, userId));
    }

    @Operation(
            summary = "인기 아이템 수정",
            description = "기존 인기 아이템을 수정합니다.<br>" +
                          "- `slotNo` 를 변경하면 슬롯 이동 로직이 적용되어 다른 아이템들의 순서가 자동으로 조정됩니다.<br>" +
                          "- `productId`, `displayYn` 변경도 가능합니다."
    )
    @PutMapping("/{id}")
    public ResponseEntity<AdminHomeBestItemResponseDto> update(
            @PathVariable Long id,
            @RequestBody @Valid AdminHomeBestItemUpdateDto UpdateDto,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        UpdateDto.setId(id);
        Long userId = (user != null) ? user.getUserId() : null;
        return ResponseEntity.ok(adminHomeBestItemService.update(UpdateDto, userId));
    }

    @Operation(
            summary = "인기 아이템 삭제",
            description = "인기 아이템을 삭제합니다.<br>" +
                          "- 삭제 시 슬롯은 그대로 남으며, 필요하다면 서비스 로직에서 슬롯 재정렬(압축)을 적용할 수 있습니다."
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        Long userId = (user != null) ? user.getUserId() : null;
        adminHomeBestItemService.delete(id, userId);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "인기 아이템 상세",
            description = "인기 아이템 상세 정보를 조회합니다.<br>" +
                          "- ID 기준으로 조회합니다."
    )
    @GetMapping("/{id}")
    public ResponseEntity<AdminHomeBestItemResponseDto> detail(@PathVariable Long id) {
        return ResponseEntity.ok(adminHomeBestItemService.detail(id));
    }

    @Operation(
            summary = "아이템 목록(페이지)",
            description = "인기 아이템 목록을 페이지 단위로 조회합니다.<br>" +
                          "- 기본 정렬: `slotNo ASC`, `id DESC`<br>" +
                          "- `keyword` 파라미터는 확장용이며 현재는 미사용 처리됩니다."
    )
    @GetMapping
    public ResponseEntity<Page<AdminHomeBestItemResponseDto>> list(
            @ParameterObject @PageableDefault(page = 0, size = 10) Pageable pageable,
            @RequestParam(required = false) String keyword
    ) {
        return ResponseEntity.ok(adminHomeBestItemService.list(pageable, keyword));
    }

}
