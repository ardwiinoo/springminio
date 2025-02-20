package com.ardwiinoo.springminio.advice;

import com.ardwiinoo.springminio.exceptions.FileStorageException;
import com.ardwiinoo.springminio.models.dto.http.response.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(FileStorageException.class)
    public ResponseEntity<BaseResponse> handleFileStorageException(FileStorageException e) {

       return new ResponseEntity<>(
               new BaseResponse(
                       null,
                          e.getMessage()
               ), HttpStatus.INTERNAL_SERVER_ERROR
       );
    }
}