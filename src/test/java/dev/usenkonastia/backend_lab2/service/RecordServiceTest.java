package dev.usenkonastia.backend_lab2.service;

import dev.usenkonastia.backend_lab2.domain.Record;
import dev.usenkonastia.backend_lab2.entity.CategoryEntity;
import dev.usenkonastia.backend_lab2.entity.RecordEntity;
import dev.usenkonastia.backend_lab2.entity.UserEntity;
import dev.usenkonastia.backend_lab2.repository.CategoryRepository;
import dev.usenkonastia.backend_lab2.repository.RecordRepository;
import dev.usenkonastia.backend_lab2.repository.UserRepository;
import dev.usenkonastia.backend_lab2.service.exception.*;
import dev.usenkonastia.backend_lab2.service.impl.RecordServiceImpl;
import dev.usenkonastia.backend_lab2.service.mapper.RecordMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@DisplayName("Record Service Tests")
@SpringBootTest(classes = RecordServiceImpl.class)
public class RecordServiceTest {

    @MockBean
    private RecordRepository recordRepository;

    @MockBean
    private CategoryRepository categoryRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RecordMapper recordMapper;

    @Autowired
    private RecordService recordService;

    private Record record;
    private CategoryEntity categoryEntity;
    private RecordEntity recordEntity;
    private UserEntity userEntity;

    @BeforeEach
    void setUp() {


        userEntity = UserEntity.builder().id(UUID.randomUUID()).email("email@example.com").build();
        categoryEntity = CategoryEntity.builder()
                .id(UUID.randomUUID())
                .user(userEntity)
                .build();
        record = Record.builder()
                .id(UUID.randomUUID())
                .userId(userEntity.getId())
                .categoryId(UUID.randomUUID())
                .expense(100.0)
                .date(ZonedDateTime.now())
                .build();

        recordEntity = RecordEntity.builder()
                .id(UUID.randomUUID())
                .user(userEntity)
                .category(CategoryEntity.builder().id(UUID.randomUUID()).build())
                .expense(100.0)
                .date(ZonedDateTime.now())
                .build();

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("email@example.com", null, List.of())
        );
    }

    @Test
    void testGetRecordById() {
        when(recordRepository.findById(any())).thenReturn(Optional.of(recordEntity));
        when(recordMapper.toRecord(recordEntity)).thenReturn(record);

        Record result = recordService.getRecordById(recordEntity.getId());

        assertThat(result).isEqualTo(record);
    }

    @Test
    void testGetRecordByIdNotFound() {
        when(recordRepository.findById(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> recordService.getRecordById(UUID.randomUUID()))
                .isInstanceOf(RecordNotFoundException.class);
    }

    @Test
    void testAddRecord() {
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(userEntity));
        when(categoryRepository.findById(any())).thenReturn(Optional.of(categoryEntity));
        when(recordMapper.toRecordEntity(any(Record.class))).thenReturn(recordEntity);
        when(recordRepository.save(any())).thenReturn(recordEntity);
        when(recordMapper.toRecord(recordEntity)).thenReturn(record);

        Record saved = recordService.addRecord(record);

        assertThat(saved).isEqualTo(record);
        verify(recordRepository).save(any(RecordEntity.class));
    }

    @Test
    void testAddRecordCategoryNotFound() {
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(userEntity));
        when(categoryRepository.findById(any())).thenReturn(Optional.empty());
        when(recordMapper.toRecordEntity(any(Record.class))).thenReturn(recordEntity);

        assertThatThrownBy(() -> recordService.addRecord(record))
                .isInstanceOf(CategoryNotFoundException.class);
    }

    @Test
    void testDeleteRecordById() {
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(userEntity));
        when(userRepository.findUserIdByRecordId(any())).thenReturn(userEntity.getId());

        recordService.deleteRecordById(record.getId());

        verify(recordRepository).deleteById(record.getId());
    }

    @Test
    void testDeleteRecordByIdNoOwner() {
        when(userRepository.findUserIdByRecordId(any())).thenReturn(null);

        recordService.deleteRecordById(record.getId());

        verify(recordRepository, never()).deleteById(any());
    }

    @Test
    void testDeleteRecordByIdForbidden() {
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(userEntity));
        when(userRepository.findUserIdByRecordId(record.getId())).thenReturn(UUID.randomUUID());

        assertThatThrownBy(() -> recordService.deleteRecordById(record.getId()))
                .isInstanceOf(ForbiddenException.class);

        verify(recordRepository, never()).deleteById(any());
    }

    @Test
    void testGetRecordsBothNull() {
        assertThatThrownBy(() -> recordService.getRecords(null, null))
                .isInstanceOf(InvalidArgumentsException.class);
    }

    @Test
    void testGetRecordsOnlyCategoryGiven() {
        when(categoryRepository.findById(any())).thenReturn(Optional.of(categoryEntity));
        when(recordRepository.findByUserIdAndCategoryId(null, categoryEntity.getId())).thenReturn(List.of(recordEntity));
        when(recordMapper.toRecordList(List.of(recordEntity))).thenReturn(List.of(record));

        List<Record> result = recordService.getRecords(null, categoryEntity.getId());

        assertThat(result).containsExactly(record);
    }

    @Test
    void testGetRecordsOnlyUserGiven() {
        when(userRepository.findById(any())).thenReturn(Optional.of(userEntity));
        when(recordRepository.findByUserIdAndCategoryId(userEntity.getId(), null)).thenReturn(List.of(recordEntity));
        when(recordMapper.toRecordList(List.of(recordEntity))).thenReturn(List.of(record));

        List<Record> result = recordService.getRecords(userEntity.getId(), null);

        assertThat(result).containsExactly(record);
    }

    @Test
    void testGetRecordsBothPresent() {
        when(userRepository.findById(any())).thenReturn(Optional.of(userEntity));
        when(recordRepository.findByUserIdAndCategoryId(any(), any())).thenReturn(List.of(recordEntity));
        when(recordMapper.toRecordList(List.of(recordEntity))).thenReturn(List.of(record));

        List<Record> result = recordService.getRecords(userEntity.getId(), categoryEntity.getId());

        assertThat(result).containsExactly(record);
    }

    @Test
    void testGetRecordsUserNotFound() {
        when(userRepository.findById(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> recordService.getRecords(UUID.randomUUID(), null))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void testGetRecordsCategoryNotFound() {
        when(categoryRepository.findById(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> recordService.getRecords(null, UUID.randomUUID()))
                .isInstanceOf(CategoryNotFoundException.class);
    }
}
