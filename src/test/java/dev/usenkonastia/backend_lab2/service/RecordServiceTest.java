package dev.usenkonastia.backend_lab2.service;

import dev.usenkonastia.backend_lab2.domain.Record;
import dev.usenkonastia.backend_lab2.entity.CategoryEntity;
import dev.usenkonastia.backend_lab2.entity.RecordEntity;
import dev.usenkonastia.backend_lab2.entity.UserEntity;
import dev.usenkonastia.backend_lab2.repository.RecordRepository;
import dev.usenkonastia.backend_lab2.security.AccessValidator;
import dev.usenkonastia.backend_lab2.security.SecurityContextService;
import dev.usenkonastia.backend_lab2.service.exception.*;
import dev.usenkonastia.backend_lab2.service.impl.RecordServiceImpl;
import dev.usenkonastia.backend_lab2.service.mapper.RecordMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Record Service Tests")
@SpringBootTest(classes = RecordServiceImpl.class)
public class RecordServiceTest {

    @MockBean
    private RecordRepository recordRepository;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private UserService userService;

    @MockBean
    private RecordMapper recordMapper;

    @MockBean
    private SecurityContextService securityContextService;

    @MockBean
    private AccessValidator accessValidator;

    @Autowired
    private RecordService recordService;

    private Record record;
    private RecordEntity recordEntity;
    private UUID userId;
    private UUID categoryId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        categoryId = UUID.randomUUID();

        record = Record.builder()
                .id(UUID.randomUUID())
                .userId(userId)
                .categoryId(categoryId)
                .expense(100.0)
                .date(ZonedDateTime.now())
                .build();

        recordEntity = RecordEntity.builder()
                .id(record.getId())
                .user(UserEntity.builder().id(userId).build())
                .category(CategoryEntity.builder().id(categoryId).build())
                .expense(100.0)
                .date(ZonedDateTime.now())
                .build();

        when(securityContextService.getCurrentUserId()).thenReturn(userId);
    }

    @Test
    void testGetRecordById_Success() {
        when(recordRepository.findById(any())).thenReturn(Optional.of(recordEntity));
        when(recordMapper.toRecord(recordEntity)).thenReturn(record);

        Record result = recordService.getRecordById(recordEntity.getId());

        assertThat(result).isEqualTo(record);
    }

    @Test
    void testGetRecordById_NotFound() {
        when(recordRepository.findById(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> recordService.getRecordById(UUID.randomUUID()))
                .isInstanceOf(RecordNotFoundException.class);
    }

    @Test
    void testAddRecord_Success() {
        when(recordMapper.toRecordEntity(any(Record.class))).thenReturn(recordEntity);
        doNothing().when(categoryService).validateCategoryExists(categoryId); // якщо метод void, тоді doNothing
        when(recordRepository.save(any(RecordEntity.class))).thenReturn(recordEntity);
        when(recordMapper.toRecord(recordEntity)).thenReturn(record);

        Record saved = recordService.addRecord(record);

        assertThat(saved).isEqualTo(record);
        verify(categoryService).validateCategoryExists(categoryId);
        verify(recordRepository).save(any());
    }

    @Test
    void testAddRecord_CategoryNotFound() {
        when(recordMapper.toRecordEntity(any(Record.class))).thenReturn(recordEntity);
        doThrow(new CategoryNotFoundException(categoryId)).when(categoryService).validateCategoryExists(categoryId);

        assertThatThrownBy(() -> recordService.addRecord(record))
                .isInstanceOf(CategoryNotFoundException.class);
    }

    @Test
    void testDeleteRecordById_Success() {
        when(recordRepository.findById(any())).thenReturn(Optional.of(recordEntity));
        when(securityContextService.getCurrentUserId()).thenReturn(userId);

        recordService.deleteRecordById(recordEntity.getId());

        verify(accessValidator).validateOwner(userId, userId);
        verify(recordRepository).deleteById(recordEntity.getId());
    }

    @Test
    void testDeleteRecordById_RecordNotFound() {
        when(recordRepository.findById(any())).thenReturn(Optional.empty());

        assertThatCode(() -> recordService.deleteRecordById(UUID.randomUUID()))
                .doesNotThrowAnyException();

        verify(recordRepository, never()).deleteById(any());
    }

    @Test
    void testGetRecords_BothNull_Throws() {
        assertThatThrownBy(() -> recordService.getRecords(null, null))
                .isInstanceOf(InvalidArgumentsException.class);
    }

    @Test
    void testGetRecords_OnlyCategoryGiven() {
        doNothing().when(categoryService).validateCategoryExists(categoryId);
        when(recordRepository.findByUserIdAndCategoryId(null, categoryId)).thenReturn(List.of(recordEntity));
        when(recordMapper.toRecordList(List.of(recordEntity))).thenReturn(List.of(record));

        List<Record> result = recordService.getRecords(null, categoryId);

        verify(categoryService).validateCategoryExists(categoryId);
        assertThat(result).containsExactly(record);
    }

    @Test
    void testGetRecords_OnlyUserGiven() {
        doNothing().when(userService).validateUserExists(userId);
        when(recordRepository.findByUserIdAndCategoryId(userId, null)).thenReturn(List.of(recordEntity));
        when(recordMapper.toRecordList(List.of(recordEntity))).thenReturn(List.of(record));

        List<Record> result = recordService.getRecords(userId, null);

        verify(userService).validateUserExists(userId);
        assertThat(result).containsExactly(record);
    }

    @Test
    void testGetRecords_BothPresent() {
        doNothing().when(userService).validateUserExists(userId);
        doNothing().when(categoryService).validateCategoryExists(categoryId);
        when(recordRepository.findByUserIdAndCategoryId(userId, categoryId)).thenReturn(List.of(recordEntity));
        when(recordMapper.toRecordList(List.of(recordEntity))).thenReturn(List.of(record));

        List<Record> result = recordService.getRecords(userId, categoryId);

        assertThat(result).containsExactly(record);
    }

    @Test
    void testGetRecords_UserNotFound() {
        doThrow(new UserNotFoundException(userId)).when(userService).validateUserExists(userId);

        assertThatThrownBy(() -> recordService.getRecords(userId, null))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void testGetRecords_CategoryNotFound() {
        doThrow(new CategoryNotFoundException(categoryId)).when(categoryService).validateCategoryExists(categoryId);

        assertThatThrownBy(() -> recordService.getRecords(null, categoryId))
                .isInstanceOf(CategoryNotFoundException.class);
    }
}
