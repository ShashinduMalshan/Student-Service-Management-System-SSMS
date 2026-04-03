package com.example.student_service_management_system.service.impl;



import com.example.student_service_management_system.dto.StudentRequestDTO;
import com.example.student_service_management_system.dto.StudentResponseDTO;
import com.example.student_service_management_system.entity.Student;
import com.example.student_service_management_system.exception.ResourceNotFoundException;
import com.example.student_service_management_system.repository.StudentRepository;
import com.example.student_service_management_system.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository repo;

    @Override
    public StudentResponseDTO addStudent(StudentRequestDTO dto) {
        Student student = new Student();
        student.setName(dto.getName());
        student.setEmail(dto.getEmail());
        student.setCourse(dto.getCourse());
        student.setCreatedAt(LocalDateTime.now());

        return mapToDTO(repo.save(student));
    }

    @Override
    public StudentResponseDTO updateStudent(Long id, StudentRequestDTO dto) {
        Student student = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        student.setName(dto.getName());
        student.setEmail(dto.getEmail());
        student.setCourse(dto.getCourse());
        student.setUpdatedAt(LocalDateTime.now());

        return mapToDTO(repo.save(student));
    }

    @Override
    public void deleteStudent(Long id) {
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("Student not found");
        }
        repo.deleteById(id);
    }

    @Override
    public List<StudentResponseDTO> getAllStudents() {
        return repo.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public StudentResponseDTO getStudentById(Long id) {
        Student student = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        return mapToDTO(student);
    }

    private StudentResponseDTO mapToDTO(Student student) {
        StudentResponseDTO dto = new StudentResponseDTO();
        dto.setId(student.getId());
        dto.setName(student.getName());
        dto.setEmail(student.getEmail());
        dto.setCourse(student.getCourse());
        dto.setCreatedAt(student.getCreatedAt());
        return dto;
    }
}