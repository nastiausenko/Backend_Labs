package dev.usenkonastia.backend_lab2.service;

import dev.usenkonastia.backend_lab2.service.exception.InvalidArgumentsException;
import dev.usenkonastia.backend_lab2.service.exception.RecordNotFoundException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import dev.usenkonastia.backend_lab2.entity.Record;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class RecordService {
    private Map<UUID, Record> records = new HashMap<>();

    public Record getRecordById(UUID id) {
        if (!records.containsKey(id)) {
            throw new RecordNotFoundException(id);
        }
        return records.get(id);
    }

    public Record addRecord(Record record) {
        Record newRecord = Record.builder()
                .id(UUID.randomUUID())
                .userId(record.getUserId())
                .categoryId(record.getCategoryId())
                .date(record.getDate() != null ? record.getDate() : ZonedDateTime.now(ZoneId.of("Europe/Kiev")))
                .expense(record.getExpense())
                .build();
        records.put(newRecord.getId(), newRecord);
        return newRecord;
    }

    public void deleteRecordById(UUID id) {
        Record record = getRecordById(id);
        records.remove(record.getId());
    }

    public List<Record> getRecords(String userId, String categoryId) {
        if (userId == null && categoryId == null) {
            throw new InvalidArgumentsException("Either userId or categoryId must be provided");
        }

        return records.values().stream()
                .filter(record -> (userId == null || record.getUserId().equals(userId)) &&
                                  (categoryId == null || record.getCategoryId().equals(categoryId)))
                .collect(Collectors.toList());
    }
}
