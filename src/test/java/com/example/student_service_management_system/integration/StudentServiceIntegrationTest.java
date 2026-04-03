package com.example.student_service_management_system.integration;

import com.example.student_service_management_system.dto.StudentRequestDTO;
import com.example.student_service_management_system.dto.StudentResponseDTO;
import com.example.student_service_management_system.service.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class StudentServiceIntegrationTest {

    @Autowired
    private StudentService service;

    @Test
    void testAddAndFetchStudent() {

        StudentRequestDTO dto = new StudentRequestDTO();
        dto.setName("Nimal Silva");
        dto.setEmail("nimal@example.lk");
        dto.setCourse("IT");

        StudentResponseDTO saved = service.addStudent(dto);

        StudentResponseDTO fetched = service.getStudentById(saved.getId());

        assertEquals("Nimal Silva", fetched.getName());
    }
}