package dev.usenkonastia.backend_lab2.repository;

import dev.usenkonastia.backend_lab2.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findByEmail(String email);

    @Query("SELECT c.user.id FROM CategoryEntity c WHERE c.id = :categoryId")
    UUID findUserIdByCategoryId(@Param("categoryId") UUID categoryId);

    @Query("SELECT r.user.id FROM RecordEntity r WHERE r.id = :recordId")
    UUID findUserIdByRecordId(@Param("recordId") UUID recordId);
}
