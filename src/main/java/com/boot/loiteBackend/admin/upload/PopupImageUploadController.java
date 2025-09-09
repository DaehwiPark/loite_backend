package com.boot.loiteBackend.admin.upload;


import com.boot.loiteBackend.common.file.FileService;
import com.boot.loiteBackend.common.file.FileUploadResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/uploads")
public class PopupImageUploadController {

    private FileService fileService;

    //팝업이미지 업로드
    public ResponseEntity<FileUploadResult> uploadPopupImage(@RequestPart("file") MultipartFile file) throws Exception{
        //저장 경로 예 : /upload/popup/연/월
        FileUploadResult r = fileService.save(file, "popup");
        return ResponseEntity.ok(r); // { urlPath, physicalPath }
    }
}
