package com.inter.clubedafabrica.controllers;

import lombok.Data;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/settings")
@CrossOrigin(origins = "*")
public class SystemSettingsController {

    private Settings settings = new Settings();

    @GetMapping
    public Settings getSettings() {
        return settings;
    }

    @PutMapping
    public Settings update(@RequestBody Settings updated) {
        settings.setStoreName(updated.getStoreName());
        settings.setContactEmail(updated.getContactEmail());
        return settings;
    }

    @Data
    public static class Settings {
        private String storeName = "Minha Loja";
        private String contactEmail = "contato@minhaloja.com";
    }
}
