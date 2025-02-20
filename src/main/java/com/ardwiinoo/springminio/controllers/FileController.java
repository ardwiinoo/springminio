package com.ardwiinoo.springminio.controllers;

import com.ardwiinoo.springminio.models.dto.file.request.FileUploadRequest;
import com.ardwiinoo.springminio.models.dto.file.response.FileResponse;
import com.ardwiinoo.springminio.models.dto.http.response.BaseResponse;
import com.ardwiinoo.springminio.services.MinioFileStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/v1/files")
public class FileController {

    private final MinioFileStorageService minioFileStorageService;

    @Autowired
    public FileController(MinioFileStorageService minioFileStorageService) {
        this.minioFileStorageService = minioFileStorageService;
    }

    @PostMapping("/upload")
    public ResponseEntity<BaseResponse> uploadFile(
            @ModelAttribute FileUploadRequest fileUploadRequest
    ) {
        minioFileStorageService.uploadFile(fileUploadRequest);

        return new ResponseEntity<>(
                new BaseResponse(
                        new FileResponse(
                                fileUploadRequest.getFile().getOriginalFilename(),
                                fileUploadRequest.getFile().getContentType(),
                                fileUploadRequest.getFile().getSize()),
                        "File uploaded with success!"
                ), HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity<BaseResponse> deleteFile(@RequestParam("fileName") String fileName) {
        minioFileStorageService.deleteFile(fileName);

        return new ResponseEntity<>(
                new BaseResponse(
                        null,
                        fileName + " has deleted with success!"
                ), HttpStatus.OK);
    }

    @GetMapping("/download")
    public ResponseEntity<InputStreamResource> downloadFile(@RequestParam("fileName") String fileName) {
        InputStreamResource resource = minioFileStorageService.downloadFile(fileName);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
