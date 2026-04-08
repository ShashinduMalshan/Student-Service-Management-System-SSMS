package com.example.student_service_management_system.controller;

import com.example.student_service_management_system.dto.StudentRequestDTO;
import com.example.student_service_management_system.dto.StudentResponseDTO;
import com.example.student_service_management_system.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@CrossOrigin(origins = "http://localhost:5179")
@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
@RestController
public class StudentController {

    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

    private final StudentService service;

    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public StudentResponseDTO addStudent(
            @RequestPart("student") StudentRequestDTO dto,
            @RequestPart(value = "image", required = false) MultipartFile image) {

        logger.info("Received request to add student: {}", dto.getName());

        // Save student first
        StudentResponseDTO response = service.addStudent(dto);

        // Upload image if provided
        if (image != null && !image.isEmpty()) {
            String imageUrl = service.updateProfile(response.getId(), image);
            response.setProfileImageUrl(imageUrl); // Include image URL in response
            logger.info("Profile image uploaded for student ID: {}", response.getId());
        }

        logger.info("Student added successfully with ID: {}", response.getId());
        return response;
    }

    @PutMapping("/{id}")
    public StudentResponseDTO updateStudent(@PathVariable Long id,
                                            @RequestBody StudentRequestDTO dto) {
        logger.info("Received request to update student with ID: {}", id);
        StudentResponseDTO response = service.updateStudent(id, dto);
        logger.info("Student updated successfully with ID: {}", response.getId());
        return response;
    }

    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable Long id) {
        logger.info("Received request to delete student with ID: {}", id);
        service.deleteStudent(id);
        logger.info("Student deleted successfully with ID: {}", id);
    }

    @GetMapping
    public Page<StudentResponseDTO> getAllStudents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        logger.info("Received request to fetch all students: page {}, size {}", page, size);
        Page<StudentResponseDTO> students = service.getAllStudents(page, size);
        logger.info("Fetched {} students", students.getNumberOfElements());
        return students;
    }

    @GetMapping("/{id}")
    public StudentResponseDTO getStudent(@PathVariable Long id) {
        logger.info("Received request to fetch student with ID: {}", id);
        StudentResponseDTO response = service.getStudentById(id);
        logger.info("Fetched student successfully with ID: {}", response.getId());
        return response;
    }

    @PatchMapping("/{id}/profile-image")
    public ResponseEntity<String> updateProfileImage(
            @PathVariable Long id,
            @RequestPart("file") MultipartFile file) {
        String imageUrl = service.updateProfile(id, file);
        return ResponseEntity.ok(imageUrl);
    }

    @DeleteMapping("/{id}/profile-image")
    public ResponseEntity<Map<String, String>> deleteProfileImage(@PathVariable Long id) {

        service.deleteProfile(id);

        return ResponseEntity.ok(Map.of("message", "Profile image deleted successfully"));
    }

}