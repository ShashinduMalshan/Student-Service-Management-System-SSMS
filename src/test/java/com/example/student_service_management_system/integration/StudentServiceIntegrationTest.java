package com.example.student_service_management_system.integration;

import com.example.student_service_management_system.dto.StudentRequestDTO;
import com.example.student_service_management_system.dto.StudentResponseDTO;
import com.example.student_service_management_system.exception.ResourceNotFoundException;
import com.example.student_service_management_system.service.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test") // Use application-test.properties with H2 database
class StudentServiceIntegrationTest {

    @Autowired
    private StudentService service;

    @Test
    void testAddAndFetchStudent() {
        // Add student
        StudentRequestDTO dto = new StudentRequestDTO();
        dto.setName("Nimal Silva");
        dto.setEmail("nimal@example.lk");
        dto.setCourse("IT");

        StudentResponseDTO saved = service.addStudent(dto);

        // Fetch student by ID
        StudentResponseDTO fetched = service.getStudentById(saved.getId());

        assertEquals("Nimal Silva", fetched.getName());
        assertEquals("nimal@example.lk", fetched.getEmail());
        assertEquals("IT", fetched.getCourse());
    }

    @Test
    void testUpdateStudent() {
        // Add a student
        StudentRequestDTO dto = new StudentRequestDTO();
        dto.setName("Kamal Perera");
        dto.setEmail("kamal@example.lk");
        dto.setCourse("CS");
        StudentResponseDTO saved = service.addStudent(dto);

        // Update student
        dto.setName("Kamal Updated");
        dto.setEmail("kamal.updated@example.lk");
        StudentResponseDTO updated = service.updateStudent(saved.getId(), dto);

        assertEquals("Kamal Updated", updated.getName());
        assertEquals("kamal.updated@example.lk", updated.getEmail());
    }

    @Test
    void testDeleteStudent() {
        // Add a student
        StudentRequestDTO dto = new StudentRequestDTO();
        dto.setName("Sunil Fernando");
        dto.setEmail("sunil@example.lk");
        dto.setCourse("IT");
        StudentResponseDTO saved = service.addStudent(dto);

        // Delete student
        service.deleteStudent(saved.getId());

        // Verify deletion
        assertThrows(ResourceNotFoundException.class, () -> service.getStudentById(saved.getId()));
    }

    @Test
    void testGetAllStudents() {
        // Add multiple students
        StudentRequestDTO dto1 = new StudentRequestDTO();
        dto1.setName("Student A");
        dto1.setEmail("a@example.lk");
        dto1.setCourse("IT");
        service.addStudent(dto1);

        StudentRequestDTO dto2 = new StudentRequestDTO();
        dto2.setName("Student B");
        dto2.setEmail("b@example.lk");
        dto2.setCourse("CS");
        service.addStudent(dto2);

        // Fetch all students
        Page<StudentResponseDTO> page = service.getAllStudents(0, 10);
        assertTrue(page.getTotalElements() >= 2);
    }
}