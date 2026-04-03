package com.example.student_service_management_system.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class StudentResponseDTO {

    private Long id;
    private String name;
    private String email;
    private String course;
    private LocalDateTime createdAt;
}