package com.example.student_service_management_system.service;

import com.example.student_service_management_system.dto.StudentRequestDTO;
import com.example.student_service_management_system.dto.StudentResponseDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface StudentService {

    StudentResponseDTO addStudent(StudentRequestDTO dto);

    StudentResponseDTO updateStudent(Long id, StudentRequestDTO dto);

    void deleteStudent(Long id);

    List<StudentResponseDTO> getAllStudents();

    StudentResponseDTO getStudentById(Long id);
}
