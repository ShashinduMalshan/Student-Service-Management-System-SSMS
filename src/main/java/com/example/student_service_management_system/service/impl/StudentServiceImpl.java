package com.example.student_service_management_system.service.impl;

import com.example.student_service_management_system.dto.StudentRequestDTO;
import com.example.student_service_management_system.dto.StudentResponseDTO;
import com.example.student_service_management_system.entity.Student;
import com.example.student_service_management_system.exception.ResourceNotFoundException;
import com.example.student_service_management_system.mapper.StudentMapper;
import com.example.student_service_management_system.repository.StudentRepository;
import com.example.student_service_management_system.service.StudentService;
import com.example.student_service_management_system.service.SupabaseService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private static final Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);

    private final StudentRepository repo;
    private final StudentMapper studentMapper;
    private final SupabaseService supabaseService;


    @Override
    public StudentResponseDTO addStudent(StudentRequestDTO dto) {
        logger.info("Adding new student with name: {}", dto.getName());
        Student student = studentMapper.toEntity(dto);
        student.setCreatedAt(LocalDateTime.now());
        Student saved = repo.save(student);
        logger.info("Student added with ID: {}", saved.getId());
        return studentMapper.toDTO(saved);
    }

    @Override
    public StudentResponseDTO updateStudent(Long id, StudentRequestDTO dto) {
        logger.info("Updating student with ID: {}", id);
        Student student = repo.findById(id)
                .orElseThrow(() -> {
                    logger.error("Student not found with ID: {}", id);
                    return new ResourceNotFoundException("Student not found");
                });
        studentMapper.updateEntity(dto, student);
        student.setUpdatedAt(LocalDateTime.now());
        Student updated = repo.save(student);
        logger.info("Student updated with ID: {}", updated.getId());
        return studentMapper.toDTO(updated);
    }

    @Override
    public void deleteStudent(Long id) {
        logger.info("Deleting student with ID: {}", id);
        if (!repo.existsById(id)) {
            logger.error("Student not found for deletion with ID: {}", id);
            throw new ResourceNotFoundException("Student not found");
        }
        repo.deleteById(id);
        logger.info("Student deleted with ID: {}", id);
    }

    @Override
    public Page<StudentResponseDTO> getAllStudents(int page, int size) {
        logger.info("Fetching all students: page {}, size {}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<StudentResponseDTO> students = repo.findAll(pageable)
                .map(studentMapper::toDTO);
        logger.info("Fetched {} students", students.getNumberOfElements());
        return students;
    }

    @Override
    public StudentResponseDTO getStudentById(Long id) {
        logger.info("Fetching student with ID: {}", id);
        Student student = repo.findById(id)
                .orElseThrow(() -> {
                    logger.error("Student not found with ID: {}", id);
                    return new ResourceNotFoundException("Student not found");
                });
        return studentMapper.toDTO(student);
    }


    @Override
    public String createProfile(Long studentId, MultipartFile file) {
        Student student = repo.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        String imageUrl = supabaseService.uploadFile(file);
        student.setProfileImageUrl(imageUrl);
        student.setUpdatedAt(LocalDateTime.now());
        repo.save(student);

        return imageUrl;
    }

    /**
     * Update a student's profile image
     */

    @Override
    public String updateProfile(Long studentId, MultipartFile file) {
        Student student = repo.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        // Optional: Delete previous image from Supabase if needed
        if (student.getProfileImageUrl() != null) {
            supabaseService.deleteFile(student.getProfileImageUrl());
        }

        String imageUrl = supabaseService.uploadFile(file);
        student.setProfileImageUrl(imageUrl);
        student.setUpdatedAt(LocalDateTime.now());
        repo.save(student);

        return imageUrl;
    }

    /**
     * Delete a student's profile image
     */

    @Override
    public void deleteProfile(Long studentId) {
        Student student = repo.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        if (student.getProfileImageUrl() != null) {
            supabaseService.deleteFile(student.getProfileImageUrl());
            student.setProfileImageUrl(null);
            student.setUpdatedAt(LocalDateTime.now());
            repo.save(student);
        }
    }
}