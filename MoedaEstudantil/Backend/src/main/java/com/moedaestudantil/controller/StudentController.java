package com.moedaestudantil.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.moedaestudantil.dto.StudentLoginRequest;
import com.moedaestudantil.dto.StudentRegisterRequest;
import com.moedaestudantil.dto.StudentResponse;
import com.moedaestudantil.dto.StudentUpdateRequest;
import com.moedaestudantil.service.StudentService;
import com.moedaestudantil.repository.TransactionRepository;
import com.moedaestudantil.repository.RedemptionRepository;
import com.moedaestudantil.model.Transaction;
import com.moedaestudantil.model.Redemption;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/students")
public class StudentController {
    
    @Autowired
    private StudentService studentService;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private RedemptionRepository redemptionRepository;
    
    @PostMapping("/register")
    public ResponseEntity<?> registerStudent(@Valid @RequestBody StudentRegisterRequest request) {
        try {
            StudentResponse response = studentService.registerStudent(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
    
    @GetMapping("/{id}/transactions")
    public ResponseEntity<?> getStudentTransactions(@PathVariable Long id) {
        try {
            List<Transaction> transactions = transactionRepository.findByStudentIdOrderByCreatedAtDesc(id);
            List<Map<String, Object>> mapped = transactions.stream().map(t -> {
                Map<String, Object> m = new HashMap<>();
                m.put("id", t.getId());
                m.put("amount", t.getAmount());
                m.put("reason", t.getReason());
                m.put("created_at", t.getCreatedAt());
                if (t.getProfessor() != null) {
                    Map<String, Object> p = new HashMap<>();
                    p.put("name", t.getProfessor().getName());
                    m.put("professors", p);
                }
                return m;
            }).toList();
            return ResponseEntity.ok(mapped);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/{id}/redemptions")
    public ResponseEntity<?> getStudentRedemptions(@PathVariable Long id) {
        try {
            List<Redemption> redemptions = redemptionRepository.findByStudent_IdOrderByCreatedAtDesc(id);
            List<Map<String, Object>> mapped = redemptions.stream().map(r -> {
                Map<String, Object> m = new HashMap<>();
                m.put("id", r.getId());
                m.put("created_at", r.getCreatedAt());
                m.put("coupon_code", r.getCouponCode());
                if (r.getAdvantage() != null) {
                    Map<String, Object> a = new HashMap<>();
                    a.put("title", r.getAdvantage().getTitle());
                    a.put("coin_cost", r.getAdvantage().getCoinCost());
                    m.put("advantages", a);
                }
                return m;
            }).toList();
            return ResponseEntity.ok(mapped);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginStudent(@Valid @RequestBody StudentLoginRequest request) {
        StudentResponse response = studentService.loginStudent(request);
        if (response.getMessage() != null) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }
    
    @GetMapping
    public ResponseEntity<List<StudentResponse>> getAllStudents() {
        List<StudentResponse> students = studentService.getAllStudents();
        return ResponseEntity.ok(students);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getStudentById(@PathVariable Long id) {
        try {
            StudentResponse response = studentService.getStudentById(id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }
    
    @GetMapping("/email/{email}")
    public ResponseEntity<?> getStudentByEmail(@PathVariable String email) {
        try {
            StudentResponse response = studentService.getStudentByEmail(email);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateStudent(@PathVariable Long id, 
                                          @RequestBody StudentUpdateRequest request) {
        try {
            StudentResponse response = studentService.updateStudent(id, request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable Long id) {
        try {
            studentService.deleteStudent(id);
            Map<String, String> success = new HashMap<>();
            success.put("message", "Aluno excluído com sucesso");
            return ResponseEntity.ok(success);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }
    
    @PatchMapping("/{id}/coins")
    public ResponseEntity<?> updateCoinBalance(@PathVariable Long id, 
                                               @RequestParam Integer amount) {
        try {
            StudentResponse response = studentService.updateCoinBalance(id, amount);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
}
