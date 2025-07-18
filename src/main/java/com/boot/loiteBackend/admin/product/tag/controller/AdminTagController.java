package com.boot.loiteBackend.admin.product.tag.controller;

import com.boot.loiteBackend.admin.product.tag.dto.AdminTagRequestDto;
import com.boot.loiteBackend.admin.product.tag.dto.AdminTagResponseDto;
import com.boot.loiteBackend.admin.product.tag.service.AdminTagService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/admin/product/tag")
@Tag(name = "Tag Management", description = "상품 태그 관리 API")
public class AdminTagController {
    private final AdminTagService adminTagService;

    @PostMapping
    public ResponseEntity<?> saveTag(@RequestBody List<AdminTagRequestDto> dtoList){
        adminTagService.saveTag(dtoList);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<AdminTagResponseDto>> getAll(){
        return ResponseEntity.ok(adminTagService.findAll());
    }

    @DeleteMapping("/{tagId}")
    public ResponseEntity<String> deleteTag(@PathVariable Long tagId){
        adminTagService.deleteTag(tagId);
        return ResponseEntity.ok("태그가 삭제되었습니다.");
    }

}
