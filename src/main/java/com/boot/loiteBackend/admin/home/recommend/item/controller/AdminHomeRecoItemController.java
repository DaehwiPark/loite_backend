package com.boot.loiteBackend.admin.home.recommend.item.controller;

import com.boot.loiteBackend.admin.home.recommend.item.dto.AdminHomeRecoItemCreateDto;
import com.boot.loiteBackend.admin.home.recommend.item.dto.AdminHomeRecoItemResponseDto;
import com.boot.loiteBackend.admin.home.recommend.item.dto.AdminHomeRecoItemUpdateDto;
import com.boot.loiteBackend.admin.home.recommend.item.service.AdminHomeRecoItemService;
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
@RequestMapping("/api/admin/home/reco/items")
@Tag(name = "홈 추천 아이템 관리 API", description = "우측 상품 카드(슬롯 1~10) CRUD")
@Validated
public class AdminHomeRecoItemController {

    private final AdminHomeRecoItemService adminHomeRecoItemService;

    @Operation(
            summary = "추천 아이템 등록",
            description = "추천 섹션에 상품을 등록합니다.<br>" +
                          "- `sectionId` 와 `productId` 를 필수로 입력합니다.<br>" +
                          "- `slotNo` 는 1~10 범위이며, 지정 슬롯이 이미 차 있으면 자동으로 뒤 슬롯들이 밀립니다.<br>" +
                          "- 최대 10개까지 등록 가능하며, 초과 시 에러가 발생합니다."
    )
    @PostMapping
    public ResponseEntity<AdminHomeRecoItemResponseDto> create(
            @RequestBody @Valid AdminHomeRecoItemCreateDto CreateDto,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        Long userId = (user != null) ? user.getUserId() : null;
        return ResponseEntity.status(HttpStatus.CREATED).body(adminHomeRecoItemService.create(CreateDto, userId));
    }

    @Operation(
            summary = "추천 아이템 수정",
            description = "기존 추천 아이템을 수정합니다.<br>" +
                          "- `slotNo` 를 변경하면 슬롯 이동 로직이 적용되어 다른 아이템들의 순서가 자동으로 조정됩니다.<br>" +
                          "- `productId`, `displayYn` 변경도 가능합니다."
    )
    @PutMapping("/{id}")
    public ResponseEntity<AdminHomeRecoItemResponseDto> update(
            @PathVariable Long id,
            @RequestBody @Valid AdminHomeRecoItemUpdateDto UpdateDto,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        UpdateDto.setId(id);
        Long userId = (user != null) ? user.getUserId() : null;
        return ResponseEntity.ok(adminHomeRecoItemService.update(UpdateDto, userId));
    }

    @Operation(
            summary = "추천 아이템 삭제",
            description = "추천 아이템을 삭제합니다.<br>" +
                          "- 삭제 시 슬롯은 그대로 남으며, 필요하다면 서비스 로직에서 슬롯 재정렬(압축)을 적용할 수 있습니다."
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        Long userId = (user != null) ? user.getUserId() : null;
        adminHomeRecoItemService.delete(id, userId);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "추천 아이템 상세",
            description = "추천 아이템 상세 정보를 조회합니다.<br>" +
                          "- ID 기준으로 조회합니다."
    )
    @GetMapping("/{id}")
    public ResponseEntity<AdminHomeRecoItemResponseDto> detail(@PathVariable Long id) {
        return ResponseEntity.ok(adminHomeRecoItemService.detail(id));
    }

    @Operation(
            summary = "아이템 목록(페이지)",
            description = "추천 아이템 목록을 페이지 단위로 조회합니다.<br>" +
                          "- 기본 정렬: `slotNo ASC`, `id DESC`<br>" +
                          "- `keyword` 파라미터는 확장용이며 현재는 미사용 처리됩니다."
    )
    @GetMapping
    public ResponseEntity<Page<AdminHomeRecoItemResponseDto>> list(
            @ParameterObject @PageableDefault(page = 0, size = 10) Pageable pageable,
            @RequestParam(required = false) String keyword
    ) {
        return ResponseEntity.ok(adminHomeRecoItemService.list(pageable, keyword));
    }
}
