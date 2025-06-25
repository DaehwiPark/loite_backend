package com.boot.loiteBackend.admin.product.tag.controller;

import com.boot.loiteBackend.admin.product.tag.dto.TagRequestDto;
import com.boot.loiteBackend.admin.product.tag.dto.TagResponseDto;
import com.boot.loiteBackend.admin.product.tag.service.TagService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/admin/product/tag")
@Tag(name = "Tag Management", description = "상품 태그 관리 API")
public class TagController {
    private final TagService tagService;

    @PostMapping
    public ResponseEntity<?> saveTag(@RequestBody List<TagRequestDto> dtoList){
        tagService.saveTag(dtoList);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<TagResponseDto>> getAll(){
        return ResponseEntity.ok(tagService.findAll());
    }

    @DeleteMapping("/{tagId}")
    public ResponseEntity<String> deleteTag(@PathVariable Long tagId){
        tagService.deleteTag(tagId);
        return ResponseEntity.ok("태그가 삭제되었습니다.");
    }

}
