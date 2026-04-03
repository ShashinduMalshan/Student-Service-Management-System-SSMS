package com.example.student_service_management_system.dto;

import lombok.Data;
import jakarta.validation.constraints.*;

@Data
public class StudentRequestDTO {

    @NotBlank
    private String name;

    @Email
    private String email;

    @NotBlank
    private String course;
}