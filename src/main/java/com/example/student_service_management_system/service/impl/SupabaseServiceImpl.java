package com.example.student_service_management_system.service.impl;

import com.example.student_service_management_system.config.SupabaseConfig;
import com.example.student_service_management_system.service.SupabaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class SupabaseServiceImpl implements SupabaseService {

    private final SupabaseConfig config;
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public String uploadFile(MultipartFile file) {

        try {
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

            String uploadUrl = config.getUrl()
                    + "/storage/v1/object/"
                    + config.getBucket()
                    + "/" + fileName;

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + config.getApiKey());
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

            HttpEntity<byte[]> request = new HttpEntity<>(file.getBytes(), headers);

            restTemplate.exchange(uploadUrl, HttpMethod.PUT, request, String.class);

            // Public URL
            return config.getUrl()
                    + "/storage/v1/object/public/"
                    + config.getBucket()
                    + "/" + fileName;

        } catch (Exception e) {
            throw new RuntimeException("Supabase upload failed");
        }
    }


    @Override
    public void deleteFile(String fileUrl) {
        try {
            // Extract file name from URL
            String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);

            String deleteUrl = config.getUrl()
                    + "/storage/v1/object/"
                    + config.getBucket()
                    + "/" + fileName;

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + config.getApiKey());

            HttpEntity<Void> request = new HttpEntity<>(headers);

            restTemplate.exchange(deleteUrl, HttpMethod.DELETE, request, String.class);

        } catch (Exception e) {
            throw new RuntimeException("Supabase delete failed: " + e.getMessage(), e);
        }
    }
}