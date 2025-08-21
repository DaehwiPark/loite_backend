package com.boot.loiteBackend.web.user.address.controller;

import com.boot.loiteBackend.config.security.CustomUserDetails;
import com.boot.loiteBackend.global.response.ApiResponse;
import com.boot.loiteBackend.web.user.address.dto.UserAddressCreateDto;
import com.boot.loiteBackend.web.user.address.dto.UserAddressDto;
import com.boot.loiteBackend.web.user.address.dto.UserAddressUpdateDto;
import com.boot.loiteBackend.web.user.address.service.UserAddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "사용자 배송지 API", description = "사용자 배송지 조회, 등록, 수정, 삭제, 기본 배송지 등록/조회 API")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/private/address")
@RequiredArgsConstructor
public class UserAddressController {

    private final UserAddressService userAddressService;

    @Operation(summary = "신규 배송지 생성", description = "사용자의 신규 배송지를 등록합니다. `isDefault=true`로 전달하면 기존 기본 배송지를 해제하고 새 주소를 기본 배송지로 지정합니다.")
    @PostMapping
    public ResponseEntity<ApiResponse<UserAddressDto>> create(@Valid @RequestBody UserAddressCreateDto req,
                                                              @AuthenticationPrincipal CustomUserDetails loginUser) {
        Long userId = loginUser.getUserId();
        UserAddressDto created = userAddressService.create(userId, req);
        return ResponseEntity.ok(ApiResponse.ok(created, "배송지 생성이 완료되었습니다."));
    }

    @Operation(summary = "배송지 수정", description = "기존 배송지를 수정합니다. `isDefault=true`로 수정하면 해당 주소가 기본 배송지로 지정되고, 기존 기본 배송지는 해제됩니다.")
    @PutMapping("/{addressId}")
    public ResponseEntity<ApiResponse<UserAddressDto>> update(@PathVariable Long addressId,
                                                              @Valid @RequestBody UserAddressUpdateDto req,
                                                              @AuthenticationPrincipal CustomUserDetails loginUser) {
        Long userId = loginUser.getUserId();
        UserAddressDto updated = userAddressService.update(userId, addressId, req);
        return ResponseEntity.ok(ApiResponse.ok(updated, "배송지 수정이 완료되었습니다."));
    }

    @Operation(summary = "배송지 삭제(소프트 삭제)", description = "특정 배송지를 소프트 삭제 처리합니다. 기본 배송지로 설정된 주소도 삭제하면 기본 플래그가 해제됩니다.")
    @DeleteMapping("/{addressId}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long addressId,
                                                    @AuthenticationPrincipal CustomUserDetails loginUser) {
        Long userId = loginUser.getUserId();
        userAddressService.delete(userId, addressId);
        return ResponseEntity.ok(ApiResponse.ok(null, "배송지 삭제가 완료되었습니다."));
    }

    @Operation(summary = "배송지 목록 조회", description = "사용자의 배송지 목록을 검색 및 페이징하여 조회합니다. 검색어(keyword)를 전달하면 수신인, 별칭, 우편번호, 주소 등에 대해 부분 검색을 수행합니다. 기본 정렬은 등록일 내림차순입니다.")
    @GetMapping
    public ResponseEntity<ApiResponse<Page<UserAddressDto>>> list(
            @Parameter(description = "검색 키워드")
            @RequestParam(required = false) String keyword,
            @ParameterObject
            @Parameter(description = "페이징 정보 (기본값: size=10, sort=faqOrder ASC)")
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails loginUser
    ) {
        Long userId = loginUser.getUserId();
        String kw = (keyword != null) ? keyword.trim() : null;
        Page<UserAddressDto> result = userAddressService.list(userId, kw, pageable);
        return ResponseEntity.ok(ApiResponse.ok(result, "조회가 완료되었습니다."));
    }

    @Operation(summary = "배송지 단건 조회", description = "특정 배송지의 상세 정보를 조회합니다.")
    @GetMapping("/{addressId}")
    public ResponseEntity<ApiResponse<UserAddressDto>> detail(@PathVariable Long addressId,
                                                              @AuthenticationPrincipal CustomUserDetails loginUser) {
        Long userId = loginUser.getUserId();
        UserAddressDto dto = userAddressService.detail(userId, addressId);
        return ResponseEntity.ok(ApiResponse.ok(dto));
    }

    @Operation(summary = "기본 배송지 조회", description = "사용자의 기본 배송지를 조회합니다. 기본 배송지가 없을 경우 데이터는 null입니다.")
    @GetMapping("/default")
    public ResponseEntity<ApiResponse<UserAddressDto>> getDefault(@AuthenticationPrincipal CustomUserDetails loginUser) {
        Long userId = loginUser.getUserId();
        UserAddressDto dto = userAddressService.getDefault(userId);
        return ResponseEntity.ok(ApiResponse.ok(dto));
    }

    @Operation(summary = "기본 배송지 설정", description = "특정 배송지를 기본 배송지로 지정합니다. 기존 기본 배송지는 자동으로 해제됩니다.")
    @PutMapping("/{addressId}/default")
    public ResponseEntity<ApiResponse<Void>> setDefault(@PathVariable Long addressId, @AuthenticationPrincipal CustomUserDetails loginUser) {
        Long userId = loginUser.getUserId();
        userAddressService.setDefault(userId, addressId);
        return ResponseEntity.ok(ApiResponse.ok(null, "기본 배송지가 설정되었습니다."));
    }
}
