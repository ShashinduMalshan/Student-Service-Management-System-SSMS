package com.example.student_service_management_system.mapper;

import com.example.student_service_management_system.dto.StudentRequestDTO;
import com.example.student_service_management_system.dto.StudentResponseDTO;
import com.example.student_service_management_system.entity.Student;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StudentMapper {

    private final ModelMapper modelMapper;

    // Convert DTO to Entity
    public Student toEntity(StudentRequestDTO dto) {
        return modelMapper.map(dto, Student.class);
    }

    // Convert Entity to DTO
    public StudentResponseDTO toDTO(Student entity) {
        return modelMapper.map(entity, StudentResponseDTO.class);
    }

    // Update existing entity with DTO data
    public void updateEntity(StudentRequestDTO dto, Student entity) {
        modelMapper.map(dto, entity);
    }
}