package dev.usenkonastia.backend_lab2.service.impl;

import dev.usenkonastia.backend_lab2.domain.Record;
import dev.usenkonastia.backend_lab2.entity.RecordEntity;
import dev.usenkonastia.backend_lab2.entity.UserEntity;
import dev.usenkonastia.backend_lab2.repository.CategoryRepository;
import dev.usenkonastia.backend_lab2.repository.RecordRepository;
import dev.usenkonastia.backend_lab2.repository.UserRepository;
import dev.usenkonastia.backend_lab2.service.RecordService;
import dev.usenkonastia.backend_lab2.service.exception.*;
import dev.usenkonastia.backend_lab2.service.mapper.RecordMapper;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RecordServiceImpl implements RecordService {
    private final RecordRepository recordRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final RecordMapper recordMapper;

    @Override
    @Transactional(readOnly = true)
    public Record getRecordById(UUID id) {
            return recordMapper.toRecord(recordRepository.findById(id).orElseThrow(() -> new RecordNotFoundException(id)));
    }

    @Override
    @Transactional
    public Record addRecord(Record record) {
            RecordEntity recordEntity = recordMapper.toRecordEntity(record);
            UUID userId = getCurrentUser();
            UUID categoryId = recordEntity.getCategory().getId();
            record = Record.builder()
                    .userId(userId)
                    .categoryId(record.getCategoryId())
                    .expense(record.getExpense())
                    .date(ZonedDateTime.now(ZoneId.of("Europe/Kiev")))
                    .build();
            categoryRepository.findById(categoryId).orElseThrow(() -> new CategoryNotFoundException(categoryId));
            return recordMapper.toRecord(recordRepository.save(recordMapper.toRecordEntity(record)));
    }

    @Override
    @Transactional
    public void deleteRecordById(UUID id) {
            UUID ownerId = userRepository.findUserIdByRecordId(id);
            if (ownerId == null) {
                return;
            }

            UUID currentUserId = getCurrentUser();
            if (!ownerId.equals(currentUserId)) {
                throw new ForbiddenException();
            }
            recordRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Record> getRecords(UUID userId, UUID categoryId) {
        if (userId == null && categoryId == null) {
            throw new InvalidArgumentsException("Either userId or categoryId must be provided");
        } else if (userId == null) {
            categoryRepository.findById(categoryId).orElseThrow(() -> new CategoryNotFoundException(categoryId));
        } else {
            userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        }
        return recordMapper.toRecordList(recordRepository.findByUserIdAndCategoryId(userId, categoryId));
    }

    private UUID getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return userRepository.findByEmail(email)
                .map(UserEntity::getId)
                .orElseThrow(() -> new UserNotFoundException(email));
    }
}
