package com.example.student_service_management_system.service;

import com.example.student_service_management_system.dto.StudentRequestDTO;
import com.example.student_service_management_system.dto.StudentResponseDTO;
import com.example.student_service_management_system.entity.Student;
import com.example.student_service_management_system.exception.ResourceNotFoundException;
import com.example.student_service_management_system.mapper.StudentMapper;
import com.example.student_service_management_system.repository.StudentRepository;
import com.example.student_service_management_system.service.impl.StudentServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;

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
                LocalDateTime.now()
        ));

        StudentResponseDTO result = service.addStudent(dto);

        assertEquals(1L, result.getId());
        verify(repo).save(student);
    }

    @Test
    void testUpdateStudent() {
        Long id = 1L;
        StudentRequestDTO dto = new StudentRequestDTO();
        dto.setName("Updated Name");

        Student existing = new Student();
        existing.setId(id);

        Student updated = new Student();
        updated.setId(id);

        when(repo.findById(id)).thenReturn(Optional.of(existing));
        doNothing().when(mapper).updateEntity(dto, existing);
        when(repo.save(existing)).thenReturn(updated);
        when(mapper.toDTO(updated)).thenReturn(new StudentResponseDTO(
                id,
                "Updated Name",
                "updated@example.lk",
                "IT",
                LocalDateTime.now()
        ));

        StudentResponseDTO result = service.updateStudent(id, dto);

        assertEquals(id, result.getId());
        verify(repo).save(existing);
    }

    @Test
    void testDeleteStudent() {
        Long id = 1L;
        when(repo.existsById(id)).thenReturn(true);
        doNothing().when(repo).deleteById(id);

        assertDoesNotThrow(() -> service.deleteStudent(id));
        verify(repo).deleteById(id);
    }

    @Test
    void testDeleteStudent_NotFound() {
        Long id = 2L;
        when(repo.existsById(id)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> service.deleteStudent(id));
    }

    @Test
    void testGetStudentById() {
        Long id = 1L;
        Student student = new Student();
        student.setId(id);

        when(repo.findById(id)).thenReturn(Optional.of(student));
        when(mapper.toDTO(student)).thenReturn(new StudentResponseDTO(
                id,
                "Kasun Perera",
                "kasun@example.lk",
                "IT",
                LocalDateTime.now()
        ));

        StudentResponseDTO result = service.getStudentById(id);

        assertEquals(id, result.getId());
        verify(repo).findById(id);
    }

    @Test
    void testGetAllStudents() {
        Student student1 = new Student();
        student1.setId(1L);
        Student student2 = new Student();
        student2.setId(2L);

        Pageable pageable = PageRequest.of(0, 5);
        Page<Student> page = new PageImpl<>(List.of(student1, student2));

        when(repo.findAll(pageable)).thenReturn(page);
        when(mapper.toDTO(student1)).thenReturn(new StudentResponseDTO(1L, "A", "a@example.lk", "IT", LocalDateTime.now()));
        when(mapper.toDTO(student2)).thenReturn(new StudentResponseDTO(2L, "B", "b@example.lk", "CS", LocalDateTime.now()));

        Page<StudentResponseDTO> result = service.getAllStudents(0, 5);

        assertEquals(2, result.getTotalElements());
        verify(repo).findAll(pageable);
    }
}