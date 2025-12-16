package com.moedaestudantil.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moedaestudantil.dto.ProfessorLoginRequest;
import com.moedaestudantil.dto.ProfessorRegisterRequest;
import com.moedaestudantil.dto.ProfessorResponse;
import com.moedaestudantil.service.ProfessorService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/professors")
public class ProfessorController {

    @Autowired
    private ProfessorService professorService;

    @PostMapping("/register")
    public ResponseEntity<?> registerProfessor(@Valid @RequestBody ProfessorRegisterRequest request) {
        try {
            ProfessorResponse response = professorService.registerProfessor(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginProfessor(@Valid @RequestBody ProfessorLoginRequest request) {
        ProfessorResponse response = professorService.loginProfessor(request);
        if (response.getMessage() != null) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ProfessorResponse>> getAllProfessors() {
        List<ProfessorResponse> list = professorService.getAllProfessors();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProfessorById(@PathVariable Long id) {
        try {
            ProfessorResponse response = professorService.getProfessorById(id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProfessor(@PathVariable Long id) {
        try {
            professorService.deleteProfessor(id);
            Map<String, String> success = new HashMap<>();
            success.put("message", "Professor excluído com sucesso");
            return ResponseEntity.ok(success);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }
}
