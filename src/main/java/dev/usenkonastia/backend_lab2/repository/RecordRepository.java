package dev.usenkonastia.backend_lab2.repository;

import dev.usenkonastia.backend_lab2.entity.RecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface RecordRepository extends JpaRepository<RecordEntity, UUID> {
    @Query("SELECT r FROM RecordEntity r WHERE " +
           "(:userId IS NULL OR r.user.id = :userId) AND " +
           "(:categoryId IS NULL OR r.category.id = :categoryId)")
    List<RecordEntity> findByUserIdAndCategoryId(@Param("userId") UUID userId,
                                                 @Param("categoryId") UUID categoryId);

}
