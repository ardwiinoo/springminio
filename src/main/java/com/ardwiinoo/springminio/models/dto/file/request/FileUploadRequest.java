package com.ardwiinoo.springminio.models.dto.file.request;

import org.springframework.web.multipart.MultipartFile;

public class FileUploadRequest {
    private MultipartFile file;

    public MultipartFile getFile() {
        return file;
    }
}
