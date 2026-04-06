package com.example.student_service_management_system.service.impl;

import com.example.student_service_management_system.config.SupabaseConfig;
import com.example.student_service_management_system.exception.SupabaseException;
import com.example.student_service_management_system.service.SupabaseService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class SupabaseServiceImpl implements SupabaseService {

    private final SupabaseConfig config;
    private static final Logger logger = LoggerFactory.getLogger(SupabaseServiceImpl.class);

    private final WebClient webClient = WebClient.builder().build();

    @Override
    public String uploadFile(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new SupabaseException("File must not be null or empty");
        }

        try {
            String fileName = URLEncoder.encode(
                    System.currentTimeMillis() + "_" + file.getOriginalFilename(),
                    StandardCharsets.UTF_8
            );

            String uploadUrl = config.getUrl() + "/storage/v1/object/" + config.getBucket() + "/" + fileName;

            logger.info("Uploading file '{}' to Supabase bucket '{}'", fileName, config.getBucket());

            webClient.put()
                    .uri(uploadUrl)
                    .header("Authorization", "Bearer " + config.getApiKey())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .bodyValue(file.getBytes())
                    .retrieve()
                    .onStatus(status -> !status.is2xxSuccessful(), response -> {
                        logger.error("Upload failed with status: {}", response.statusCode());
                        return response.createException();
                    })
                    .bodyToMono(String.class)
                    .block(); // blocking because your app is not reactive

            String publicUrl = config.getUrl() + "/storage/v1/object/public/" + config.getBucket() + "/" + fileName;

            logger.info("File uploaded successfully. Public URL: {}", publicUrl);

            return publicUrl;

        } catch (Exception e) {
            logger.error("Supabase upload failed for file '{}'", file.getOriginalFilename(), e);
            throw new SupabaseException("Supabase upload failed for file: " + file.getOriginalFilename(), e);
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

            webClient.delete()
                    .uri(deleteUrl)
                    .header("Authorization", "Bearer " + config.getApiKey())
                    .retrieve()
                    .onStatus(status -> !status.is2xxSuccessful(), response -> {
                        logger.error("Delete failed with status: {}", response.statusCode());
                        return response.createException();
                    })
                    .bodyToMono(String.class)
                    .block();

            logger.info("File '{}' deleted successfully", fileName);

        } catch (Exception e) {
            logger.error("Supabase delete failed for file '{}'", fileUrl, e);
            throw new SupabaseException("Supabase delete failed for file: " + fileUrl, e);
        }
    }
}