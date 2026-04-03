package com.example.student_service_management_system.service.impl;

import com.example.student_service_management_system.dto.StudentRequestDTO;
import com.example.student_service_management_system.dto.StudentResponseDTO;
import com.example.student_service_management_system.entity.Student;
import com.example.student_service_management_system.exception.ResourceNotFoundException;
import com.example.student_service_management_system.repository.StudentRepository;
import com.example.student_service_management_system.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository repo;
    private final ModelMapper modelMapper;

    @Override
    public StudentResponseDTO addStudent(StudentRequestDTO dto) {
        Student student = modelMapper.map(dto, Student.class);
        student.setCreatedAt(LocalDateTime.now());
        Student saved = repo.save(student);
        return modelMapper.map(saved, StudentResponseDTO.class);
    }

    @Override
    public StudentResponseDTO updateStudent(Long id, StudentRequestDTO dto) {
        Student student = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        modelMapper.map(dto, student); // Updates fields automatically
        student.setUpdatedAt(LocalDateTime.now());
        Student updated = repo.save(student);
        return modelMapper.map(updated, StudentResponseDTO.class);
    }

    @Override
    public void deleteStudent(Long id) {
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("Student not found");
        }
        repo.deleteById(id);
    }

    @Override
    public Page<StudentResponseDTO> getAllStudents(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repo.findAll(pageable)
                .map(student -> modelMapper.map(student, StudentResponseDTO.class));
    }

    @Override
    public StudentResponseDTO getStudentById(Long id) {
        Student student = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
        return modelMapper.map(student, StudentResponseDTO.class);
    }
}