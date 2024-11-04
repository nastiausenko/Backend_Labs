package dev.usenkonastia.backend_lab2.web;

import dev.usenkonastia.backend_lab2.entity.Record;
import dev.usenkonastia.backend_lab2.service.RecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1/record")
@RequiredArgsConstructor
public class RecordController {
    private final RecordService recordService;

    @GetMapping("/{id}")
    public ResponseEntity<Record> getRecordById(@PathVariable UUID id) {
        return ResponseEntity.ok(recordService.getRecordById(id));
    }

    @GetMapping
    public ResponseEntity<List<Record>> getRecords(@RequestParam(required = false) String userId,
                                                   @RequestParam(required = false) String categoryId) {
        return ResponseEntity.ok(recordService.getRecords(userId, categoryId));
    }

    @PostMapping
    public ResponseEntity<Record> addRecord(@RequestBody Record record) {
        return ResponseEntity.ok(recordService.addRecord(record));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Record> deleteRecord(@PathVariable UUID id) {
        recordService.deleteRecordById(id);
        return ResponseEntity.noContent().build();
    }
}
