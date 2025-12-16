package com.moedaestudantil.service;

import com.moedaestudantil.dto.CompanyLoginRequest;
import com.moedaestudantil.dto.CompanyRegisterRequest;
import com.moedaestudantil.dto.CompanyResponse;
import com.moedaestudantil.model.Company;
import com.moedaestudantil.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class CompanyService {
    
    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private PasswordEncoder encoder;
    
    public CompanyResponse register(CompanyRegisterRequest request) {
        if (companyRepository.existsByEmail(request.getEmail())) {
            return new CompanyResponse("Email já cadastrado");
        }
        
        if (companyRepository.existsByCnpj(request.getCnpj())) {
            return new CompanyResponse("CNPJ já cadastrado");
        }
        
        Company company = new Company();
        company.setCompanyName(request.getCompanyName());
        company.setCnpj(request.getCnpj());
        company.setEmail(request.getEmail());
        company.setPassword(encoder.encode(request.getPassword())); // Em produção, usar BCrypt
        
        company = companyRepository.save(company);
        
        return new CompanyResponse(company.getId(), company.getCompanyName(), 
                                 company.getCnpj(), company.getEmail());
    }
    
    public CompanyResponse login(CompanyLoginRequest request) {
        Optional<Company> companyOpt = companyRepository.findByEmail(request.getEmail());
        
        if (companyOpt.isEmpty()) {
            return new CompanyResponse("Credenciais inválidas");
        }
        
        Company company = companyOpt.get();
        
        if (!encoder.matches(request.getPassword(), company.getPassword())) {
            return new CompanyResponse("Credenciais inválidas");
        }
        
        return new CompanyResponse(company.getId(), company.getCompanyName(), 
                                 company.getCnpj(), company.getEmail());
    }
    
    public Optional<Company> getCompanyById(Long id) {
        return companyRepository.findById(id);
    }
    
    public Optional<Company> getCompanyByEmail(String email) {
        return companyRepository.findByEmail(email);
    }
}
