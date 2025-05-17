package dev.usenkonastia.backend_lab2.service;

import dev.usenkonastia.backend_lab2.domain.Record;

import java.util.List;
import java.util.UUID;

public interface RecordService {
    Record getRecordById(UUID id);
    Record addRecord(Record record);
    void deleteRecordById(UUID id);
    List<Record> getRecords(UUID userId, UUID categoryId);
}
