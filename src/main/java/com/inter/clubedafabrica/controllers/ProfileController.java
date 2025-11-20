package com.inter.clubedafabrica.controllers;

import com.inter.clubedafabrica.entities.Profile;
import com.inter.clubedafabrica.services.ProfileService;
import com.inter.clubedafabrica.repositories.ProfileRepository;
import lombok.RequiredArgsConstructor;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    // UPDATE PROFILE
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

            if (updates.getName() != null) existing.setName(updates.getName());
            if (updates.getPhone() != null) existing.setPhone(updates.getPhone());
            if (updates.getCpf() != null) existing.setCpf(updates.getCpf());
            if (updates.getAvatarUrl() != null) existing.setAvatarUrl(updates.getAvatarUrl());
            if (updates.getStatus() != null) existing.setStatus(updates.getStatus());
            // se não tiver userType, deixa comentado
            // if (updates.getUserType() != null) existing.setUserType(updates.getUserType());

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
