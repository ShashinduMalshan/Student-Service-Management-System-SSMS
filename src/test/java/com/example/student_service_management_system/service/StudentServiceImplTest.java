package com.example.student_service_management_system.service;

import com.example.student_service_management_system.dto.StudentRequestDTO;
import com.example.student_service_management_system.dto.StudentResponseDTO;
import com.example.student_service_management_system.entity.Student;
import com.example.student_service_management_system.mapper.StudentMapper;
import com.example.student_service_management_system.repository.StudentRepository;
import com.example.student_service_management_system.service.impl.StudentServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class StudentServiceImplTest {

    @Mock
    private StudentRepository repo;

    @Mock
    private StudentMapper mapper;

    @InjectMocks
    private StudentServiceImpl service;

    @Test
    void testAddStudent() {

        StudentRequestDTO dto = new StudentRequestDTO();
        LocalDateTime now = LocalDateTime.of(2024, 1, 1, 10, 0);

        dto.setName("Kasun Perera");

        Student student = new Student();
        Student saved = new Student();
        saved.setId(1L);

        when(mapper.toEntity(dto)).thenReturn(student);
        when(repo.save(student)).thenReturn(saved);
        when(mapper.toDTO(saved)).thenReturn(new StudentResponseDTO(
                1L,
                "Kasun Perera",
                "kasun@example.lk",
                "IT",
                now
        ));

        StudentResponseDTO result = service.addStudent(dto);

        assertEquals(1L, result.getId());
        verify(repo).save(student);
    }
}