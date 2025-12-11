package org.example.healthcare.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.healthcare.service.TranslationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/translate")
@Tag(name = "Translation", description = "Text translation services (Arabic ↔ English)")
@PreAuthorize("hasAnyRole('PATIENT', 'DOCTOR', 'ADMIN', 'NGO')")
public class TranslationController {
    
    @Autowired
    private TranslationService translationService;
    
    @PostMapping
    @Operation(summary = "Translate text", description = "Translate text between Arabic and English")
    public ResponseEntity<Map<String, String>> translate(
            @RequestBody Map<String, String> request) {
        String text = request.get("text");
        String sourceLang = request.get("sourceLang");
        String targetLang = request.get("targetLang");
        
        String translated = translationService.translate(text, sourceLang, targetLang);
        
        return ResponseEntity.ok(Map.of(
            "originalText", text,
            "translatedText", translated,
            "sourceLang", sourceLang,
            "targetLang", targetLang
        ));
    }
    
    @PostMapping("/ar-to-en")
    @Operation(summary = "Translate Arabic to English")
    public ResponseEntity<Map<String, String>> translateArabicToEnglish(
            @RequestBody Map<String, String> request) {
        String text = request.get("text");
        String translated = translationService.translateArabicToEnglish(text);
        
        return ResponseEntity.ok(Map.of(
            "originalText", text,
            "translatedText", translated
        ));
    }
    
    @PostMapping("/en-to-ar")
    @Operation(summary = "Translate English to Arabic")
    public ResponseEntity<Map<String, String>> translateEnglishToArabic(
            @RequestBody Map<String, String> request) {
        String text = request.get("text");
        String translated = translationService.translateEnglishToArabic(text);
        
        return ResponseEntity.ok(Map.of(
            "originalText", text,
            "translatedText", translated
        ));
    }
}

