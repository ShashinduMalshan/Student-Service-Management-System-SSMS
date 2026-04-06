package com.example.student_service_management_system.service.impl;

import com.example.student_service_management_system.config.SupabaseConfig;
import com.example.student_service_management_system.exception.SupabaseException;
import com.example.student_service_management_system.service.SupabaseService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class SupabaseServiceImpl implements SupabaseService {

    private final SupabaseConfig config;
    private final RestTemplate restTemplate = new RestTemplate();
    private static final Logger logger = LoggerFactory.getLogger(SupabaseServiceImpl.class);

    @Override
    public String uploadFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new SupabaseException("File must not be null or empty");
        }

        try {
            String fileName = URLEncoder.encode(System.currentTimeMillis() + "_" + file.getOriginalFilename(),
                    StandardCharsets.UTF_8);

            String uploadUrl = config.getUrl() + "/storage/v1/object/" + config.getBucket() + "/" + fileName;
            logger.info("Uploading file '{}' to Supabase bucket '{}'", fileName, config.getBucket());

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + config.getApiKey());
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

            HttpEntity<byte[]> request = new HttpEntity<>(file.getBytes(), headers);
            ResponseEntity<String> response = restTemplate.exchange(uploadUrl, HttpMethod.PUT, request, String.class);

            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new SupabaseException("Failed to upload file, Supabase responded with: " + response.getStatusCode());
            }

            String publicUrl = config.getUrl() + "/storage/v1/object/public/" + config.getBucket() + "/" + fileName;
            logger.info("File uploaded successfully. Public URL: {}", publicUrl);

            return publicUrl;

        } catch (RestClientException e) {
            logger.error("Supabase REST client error for file '{}'", file.getOriginalFilename(), e);
            throw new SupabaseException("Supabase upload failed for file: " + file.getOriginalFilename(), e);
        } catch (Exception e) {
            logger.error("Unexpected error uploading file '{}'", file.getOriginalFilename(), e);
            throw new SupabaseException("Unexpected error during Supabase upload for file: " + file.getOriginalFilename(), e);
        }
    }

    @Override
    public void deleteFile(String fileUrl) {
        if (fileUrl == null || fileUrl.isEmpty()) {
            throw new SupabaseException("File URL must not be null or empty");
        }

        try {
            String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
            String deleteUrl = config.getUrl() + "/storage/v1/object/" + config.getBucket() + "/" + fileName;

            logger.info("Deleting file '{}' from Supabase bucket '{}'", fileName, config.getBucket());

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + config.getApiKey());
            HttpEntity<Void> request = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(deleteUrl, HttpMethod.DELETE, request, String.class);
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new SupabaseException("Failed to delete file, Supabase responded with: " + response.getStatusCode());
            }

            logger.info("File '{}' deleted successfully", fileName);

        } catch (RestClientException e) {
            logger.error("Supabase REST client error while deleting file '{}'", fileUrl, e);
            throw new SupabaseException("Supabase delete failed for file: " + fileUrl, e);
        } catch (Exception e) {
            logger.error("Unexpected error while deleting file '{}'", fileUrl, e);
            throw new SupabaseException("Unexpected error during Supabase delete for file: " + fileUrl, e);
        }
    }
}