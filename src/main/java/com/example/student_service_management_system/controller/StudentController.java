package com.example.student_service_management_system.controller;

import com.example.student_service_management_system.dto.StudentRequestDTO;
import com.example.student_service_management_system.dto.StudentResponseDTO;
import com.example.student_service_management_system.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/students")
@RequiredArgsConstructor
@RestController
public class StudentController {

    private final StudentService service;

    @PostMapping
    public StudentResponseDTO addStudent(@RequestBody StudentRequestDTO dto) {
        return service.addStudent(dto);
    }

    @PutMapping("/{id}")
    public StudentResponseDTO updateStudent(@PathVariable Long id,
                                            @RequestBody StudentRequestDTO dto) {
        return service.updateStudent(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable Long id) {
        service.deleteStudent(id);
    }

    @GetMapping
    public Page<StudentResponseDTO> getAllStudents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        return service.getAllStudents(page, size);
    }

    @GetMapping("/{id}")
    public StudentResponseDTO getStudent(@PathVariable Long id) {
        return service.getStudentById(id);
    }
}
