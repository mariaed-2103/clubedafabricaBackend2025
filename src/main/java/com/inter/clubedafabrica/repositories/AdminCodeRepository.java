package com.inter.clubedafabrica.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.inter.clubedafabrica.entities.AdminCode;

import jakarta.transaction.Transactional;

@Repository
public interface AdminCodeRepository extends JpaRepository<AdminCode, Long> {
    
    // Verifica se existe um código que ainda não foi usado
    boolean existsByCodeAndIsUsedFalse(String code);

     // Atualiza um código de administrador para marcar como usado
    @Modifying
    @Transactional
    @Query("UPDATE AdminCode a SET a.isUsed = true, a.usedBy = :userId WHERE a.code = :code")
    void markAsUsed(String code, Long userId);
}

