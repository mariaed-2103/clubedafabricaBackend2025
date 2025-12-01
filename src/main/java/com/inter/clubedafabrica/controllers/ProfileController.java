package com.inter.clubedafabrica.controllers;

import com.inter.clubedafabrica.entities.Profile;
import com.inter.clubedafabrica.services.ProfileService;
import com.inter.clubedafabrica.repositories.ProfileRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(
    origins = "http://localhost:5173",
    allowedHeaders = "*",
    methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PATCH, RequestMethod.DELETE, RequestMethod.OPTIONS}
)
@RestController
@RequestMapping("/api/profiles")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileRepository repo;
    private final ProfileService service;

    // GET ALL
    @GetMapping
    public ResponseEntity<List<Profile>> getAll() {
        return ResponseEntity.ok(repo.findAll());
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return repo.findById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(404).body("Usuário não encontrado"));
    }

    // GET STATUS
    @GetMapping("/{id}/status")
    public ResponseEntity<String> getStatus(@PathVariable Long id) {
        return ResponseEntity.ok(service.getStatus(id));
    }

    // UPDATE PROFILE (ativar/desativar usuário)
    @PatchMapping("/{id}")
    public ResponseEntity<?> updateProfile(
            @PathVariable Long id,
            @RequestBody Profile updates
    ) {
        Optional<Profile> optional = repo.findById(id);

        if (optional.isEmpty()) {
            return ResponseEntity.status(404).body("Usuário não encontrado");
        }

        Profile existing = optional.get();

        // Atualizar nome
        if (updates.getName() != null && !updates.getName().isBlank()) {
            existing.setName(updates.getName());
        }

        // Atualizar email
        if (updates.getEmail() != null && !updates.getEmail().isBlank()) {

            // Impedir e-mail duplicado
            if (repo.findByEmail(updates.getEmail())
                    .filter(u -> !u.getId().equals(existing.getId()))
                    .isPresent()) {
                return ResponseEntity.status(400).body("E-mail já está em uso.");
            }

            existing.setEmail(updates.getEmail());
        }

        // Atualizar telefone
        if (updates.getPhone() != null && !updates.getPhone().isBlank()) {
            String cleanPhone = updates.getPhone().replaceAll("\\D", "");
            existing.setPhone(cleanPhone);
        }

        // Atualizar avatarUrl
        if (updates.getAvatarUrl() != null) {
            existing.setAvatarUrl(updates.getAvatarUrl());
        }

        // updatedAt
        existing.setUpdatedAt(java.time.LocalDateTime.now());

        repo.save(existing);

        return ResponseEntity.ok(existing);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        if (!repo.existsById(id)) {
            return ResponseEntity.status(404).body("Usuário não encontrado");
        }
        repo.deleteById(id);
        return ResponseEntity.ok("Usuário removido com sucesso");
    }
}
