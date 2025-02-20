package com.ardwiinoo.springminio.services.impl;

import com.ardwiinoo.springminio.exceptions.FileStorageException;
import com.ardwiinoo.springminio.models.dto.file.request.FileUploadRequest;
import com.ardwiinoo.springminio.services.MinioFileStorageService;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class MinioFileStorageServiceImpl implements MinioFileStorageService {

    private final MinioClient minioClient;

    @Value("${minio.bucket-name}")
    private String bucketName;

    public MinioFileStorageServiceImpl(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    @Async
    @Override
    public void uploadFile(FileUploadRequest request) {
        try {
            var putObjectRequest = PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(request.getFile().getOriginalFilename())
                    .stream(request.getFile().getInputStream(), request.getFile().getSize(), -1)
                    .contentType(request.getFile().getContentType())
                    .build();

            minioClient.putObject(putObjectRequest);

        } catch (Exception e ) {
            throw new FileStorageException("Could not upload file, " + e.getMessage());
        }
    }

    @Override
    public InputStreamResource downloadFile(String fileName) {
        try {
            var inputStream = minioClient.getObject(GetObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .build());

            return new InputStreamResource(inputStream);

        } catch (Exception e) {
            throw new FileStorageException("Could not download file, " + e.getMessage());
        }
    }

    @Async
    @Override
    public void deleteFile(String fileName) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .build());

        } catch (Exception e) {
            throw new FileStorageException("Could not delete file, " + e.getMessage());
        }
    }
}