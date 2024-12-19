package dev.usenkonastia.backend_lab2.web;

import dev.usenkonastia.backend_lab2.entity.RecordEntity;
import dev.usenkonastia.backend_lab2.service.RecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/api/v1/record")
@RequiredArgsConstructor
public class RecordController {
    private final RecordService recordService;

    @GetMapping("/{id}")
    public ResponseEntity<RecordEntity> getRecordById(@PathVariable UUID id) {
        return ResponseEntity.ok(recordService.getRecordById(id));
    }

    @PostMapping
    public ResponseEntity<RecordEntity> addRecord(@RequestBody RecordEntity record) {
        return ResponseEntity.ok(recordService.addRecord(record));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RecordEntity> deleteRecord(@PathVariable UUID id) {
        recordService.deleteRecordById(id);
        return ResponseEntity.noContent().build();
    }
}
