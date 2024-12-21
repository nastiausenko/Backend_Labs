package dev.usenkonastia.backend_lab2.web;

import dev.usenkonastia.backend_lab2.dto.record.RecordDto;
import dev.usenkonastia.backend_lab2.dto.record.RecordListDto;
import dev.usenkonastia.backend_lab2.entity.RecordEntity;
import dev.usenkonastia.backend_lab2.service.RecordService;
import dev.usenkonastia.backend_lab2.service.mapper.RecordMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@Validated
@Tag(name = "Record")
@RequestMapping("/api/v1/record")
@RequiredArgsConstructor
public class RecordController {
    private final RecordService recordService;
    private final RecordMapper recordMapper;

    @GetMapping("/{id}")
    public ResponseEntity<RecordDto> getRecordById(@PathVariable UUID id) {
        return ResponseEntity.ok(recordMapper.toRecordDto(recordService.getRecordById(id)));
    }

    @PostMapping
    public ResponseEntity<RecordDto> addRecord(@Valid @RequestBody RecordDto record) {
        return ResponseEntity.ok(recordMapper.toRecordDto(recordService.addRecord(recordMapper.toRecord(record))));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RecordEntity> deleteRecord(@PathVariable UUID id) {
        recordService.deleteRecordById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<RecordListDto> getRecords(@RequestParam(required = false) UUID userId,
                                                    @RequestParam(required = false) UUID categoryId) {
        return ResponseEntity.ok(recordMapper.toRecordListDto(recordService.getRecords(userId, categoryId)));
    }
}
