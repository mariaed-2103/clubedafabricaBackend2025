package com.inter.clubedafabrica.repositories;

import com.inter.clubedafabrica.entities.AdminCode;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;

@Repository
public interface AdminCodeRepository extends JpaRepository<AdminCode, Long> {

    boolean existsByCodeAndIsUsedFalse(String code);
    Optional<AdminCode> findByCode(String code);
    
    @Modifying
    @Transactional
    @Query("UPDATE AdminCode a SET a.isUsed = true, a.usedBy = :userId, a.usedAt = CURRENT_TIMESTAMP WHERE a.code = :code")
    void markAsUsed(String code, Long userId);

    Optional<AdminCode> findByCodeAndIsUsedFalse(String code);

}
