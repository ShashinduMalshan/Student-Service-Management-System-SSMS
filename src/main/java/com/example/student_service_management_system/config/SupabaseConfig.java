package com.example.student_service_management_system.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

    @Getter
    @Configuration
    public class SupabaseConfig {

        @Value("${supabase.url}")
        private String url;

        @Value("${supabase.api.key}")
        private String apiKey;

        @Value("${supabase.bucket}")
        private String bucket;

    }
