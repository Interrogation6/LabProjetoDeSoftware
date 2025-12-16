package com.moedaestudantil.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moedaestudantil.model.Institution;
import com.moedaestudantil.repository.InstitutionRepository;

@RestController
@RequestMapping("/api/institutions")
public class InstitutionController {
    
    @Autowired
    private InstitutionRepository institutionRepository;
    
    @GetMapping
    public ResponseEntity<List<Institution>> getAllInstitutions() {
        List<Institution> institutions = institutionRepository.findAll();
        return ResponseEntity.ok(institutions);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getInstitutionById(@PathVariable Long id) {
        return institutionRepository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
}
