package com.ardwiinoo.springminio.services;

import com.ardwiinoo.springminio.models.dto.file.request.FileUploadRequest;
import org.springframework.core.io.InputStreamResource;

public interface MinioFileStorageService {
    void uploadFile(FileUploadRequest request);
    InputStreamResource downloadFile(String fileName);
    void deleteFile(String fileName);
}
