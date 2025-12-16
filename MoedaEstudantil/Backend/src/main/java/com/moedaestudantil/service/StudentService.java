package com.moedaestudantil.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.moedaestudantil.dto.StudentLoginRequest;
import com.moedaestudantil.dto.StudentRegisterRequest;
import com.moedaestudantil.dto.StudentResponse;
import com.moedaestudantil.dto.StudentUpdateRequest;
import com.moedaestudantil.model.Institution;
import com.moedaestudantil.model.Student;
import com.moedaestudantil.repository.InstitutionRepository;
import com.moedaestudantil.repository.StudentRepository;

@Service
public class StudentService {
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private InstitutionRepository institutionRepository;

    @Autowired
    private PasswordEncoder encoder;
    
    @Transactional
    public StudentResponse registerStudent(StudentRegisterRequest request) {
        // Verificar se email já existe
        if (studentRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email já cadastrado");
        }
        
        // Verificar se CPF já existe
        if (studentRepository.existsByCpf(request.getCpf())) {
            throw new RuntimeException("CPF já cadastrado");
        }
        
        // Buscar instituição
        Institution institution = institutionRepository.findById(request.getInstitutionId())
            .orElseThrow(() -> new RuntimeException("Instituição não encontrada"));
        
        // Criar novo aluno
        Student student = new Student(
            request.getName(),
            request.getEmail(),
            request.getCpf(),
            request.getRg(),
            request.getAddress(),
            institution,
            request.getCourse(),
            encoder.encode(request.getPassword()) // Em produção, seria necessário hashear a senha
        );
        
        Student savedStudent = studentRepository.save(student);
        return new StudentResponse(savedStudent);
    }
    
    public StudentResponse loginStudent(StudentLoginRequest request) {
        return studentRepository.findByEmail(request.getEmail())
            .map(student -> {
                if(!encoder.matches(request.getPassword(), student.getPassword())) {
                    return new StudentResponse("Credenciais inválidas");
                }
                return new StudentResponse(student);
            })
            .orElseGet(() -> new StudentResponse("Credenciais inválidas"));
    }
    
    public List<StudentResponse> getAllStudents() {
        return studentRepository.findAll().stream()
            .map(StudentResponse::new)
            .collect(Collectors.toList());
    }
    
    public StudentResponse getStudentById(Long id) {
        Student student = studentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));
        return new StudentResponse(student);
    }
    
    public StudentResponse getStudentByEmail(String email) {
        Student student = studentRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));
        return new StudentResponse(student);
    }
    
    @Transactional
    public StudentResponse updateStudent(Long id, StudentUpdateRequest request) {
        Student student = studentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));
        
        // Atualizar apenas os campos que foram fornecidos
        if (request.getName() != null && !request.getName().trim().isEmpty()) {
            student.setName(request.getName());
        }
        
        if (request.getEmail() != null && !request.getEmail().trim().isEmpty()) {
            // Verificar se o novo email já existe (exceto para o próprio aluno)
            if (!request.getEmail().equals(student.getEmail()) && 
                studentRepository.existsByEmail(request.getEmail())) {
                throw new RuntimeException("Email já cadastrado");
            }
            student.setEmail(request.getEmail());
        }
        
        if (request.getAddress() != null && !request.getAddress().trim().isEmpty()) {
            student.setAddress(request.getAddress());
        }
        
        if (request.getCourse() != null && !request.getCourse().trim().isEmpty()) {
            student.setCourse(request.getCourse());
        }
        
        if (request.getPassword() != null && !request.getPassword().trim().isEmpty()) {
            student.setPassword(encoder.encode(request.getPassword())); // Em produção, hashear a senha
        }
        
        Student updatedStudent = studentRepository.save(student);
        return new StudentResponse(updatedStudent);
    }
    
    @Transactional
    public void deleteStudent(Long id) {
        Student student = studentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));
        studentRepository.delete(student);
    }
    
    @Transactional
    public StudentResponse updateCoinBalance(Long id, Integer amount) {
        Student student = studentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));
        
        int newBalance = student.getCoinBalance() + amount;
        if (newBalance < 0) {
            throw new RuntimeException("Saldo insuficiente");
        }
        
        student.setCoinBalance(newBalance);
        Student updatedStudent = studentRepository.save(student);
        return new StudentResponse(updatedStudent);
    }
}
