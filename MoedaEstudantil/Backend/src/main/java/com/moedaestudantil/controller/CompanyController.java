package com.moedaestudantil.controller;

import com.moedaestudantil.dto.CompanyLoginRequest;
import com.moedaestudantil.dto.CompanyRegisterRequest;
import com.moedaestudantil.dto.CompanyResponse;
import com.moedaestudantil.service.CompanyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/company")
public class CompanyController {
    
    @Autowired
    private CompanyService companyService;
    
    @PostMapping("/register")
    public ResponseEntity<CompanyResponse> register(@Valid @RequestBody CompanyRegisterRequest request) {
        CompanyResponse response = companyService.register(request);
        
        if (response.getMessage() != null) {
            return ResponseEntity.badRequest().body(response);
        }
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/login")
    public ResponseEntity<CompanyResponse> login(@Valid @RequestBody CompanyLoginRequest request) {
        CompanyResponse response = companyService.login(request);
        
        if (response.getMessage() != null) {
            return ResponseEntity.badRequest().body(response);
        }
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<CompanyResponse> getCompany(@PathVariable Long id) {
        return companyService.getCompanyById(id)
                .map(company -> ResponseEntity.ok(new CompanyResponse(
                    company.getId(), 
                    company.getCompanyName(), 
                    company.getCnpj(), 
                    company.getEmail()
                )))
                .orElse(ResponseEntity.notFound().build());
    }
}
