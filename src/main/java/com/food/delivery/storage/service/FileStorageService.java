package com.food.delivery.storage.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    String uploadFile(MultipartFile file);
    boolean deleteFile(String fileName);

}