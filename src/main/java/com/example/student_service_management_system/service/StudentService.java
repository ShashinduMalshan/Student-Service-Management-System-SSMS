package com.example.student_service_management_system.service;

import com.example.student_service_management_system.dto.StudentRequestDTO;
import com.example.student_service_management_system.dto.StudentResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface StudentService {

    StudentResponseDTO addStudent(StudentRequestDTO dto);

    StudentResponseDTO updateStudent(Long id, StudentRequestDTO dto);

    void deleteStudent(Long id);

    Page<StudentResponseDTO> getAllStudents(int page, int size);

    StudentResponseDTO getStudentById(Long id);

    String createProfile(Long studentId, MultipartFile file);

    String updateProfile(Long studentId, MultipartFile file);

    void deleteProfile(Long studentId);
}
