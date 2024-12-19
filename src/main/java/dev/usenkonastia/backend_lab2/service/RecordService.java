package dev.usenkonastia.backend_lab2.service;

import dev.usenkonastia.backend_lab2.entity.RecordEntity;
import dev.usenkonastia.backend_lab2.repository.RecordRepository;
import dev.usenkonastia.backend_lab2.service.exception.RecordNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RecordService {
    private final RecordRepository recordRepository;

    public RecordEntity getRecordById(UUID id) {
        return recordRepository.findById(id).orElseThrow(() -> new RecordNotFoundException(id));
    }

    public RecordEntity addRecord(RecordEntity record) {
        return recordRepository.save(record);
    }

    public void deleteRecordById(UUID id) {
        recordRepository.deleteById(id);
    }

    public List<RecordEntity> getRecords(UUID userId, UUID categoryId) {
        return recordRepository.findAllByUser_IdAndCategory_Id(userId, categoryId);
    }
}
