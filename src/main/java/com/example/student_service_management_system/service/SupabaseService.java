package com.example.student_service_management_system.service;

import org.springframework.web.multipart.MultipartFile;

public interface SupabaseService {
    String uploadFile(MultipartFile file);

    void deleteFile(String fileUrl);
}
