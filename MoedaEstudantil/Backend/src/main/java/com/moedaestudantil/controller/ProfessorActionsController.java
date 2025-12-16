package com.moedaestudantil.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.moedaestudantil.model.Professor;
import com.moedaestudantil.model.Student;
import com.moedaestudantil.model.Transaction;
import com.moedaestudantil.repository.ProfessorRepository;
import com.moedaestudantil.repository.StudentRepository;
import com.moedaestudantil.repository.TransactionRepository;

@RestController
@RequestMapping("/api/professor")
@CrossOrigin(origins = "*")
public class ProfessorActionsController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @GetMapping("/students/{institutionId}")
    public ResponseEntity<?> getStudentsByInstitution(@PathVariable Long institutionId) {
        try {
            List<Student> students = studentRepository.findByInstitutionId(institutionId);
            // Map to lightweight DTO
            List<Map<String, Object>> mapped = students.stream().map(s -> {
                Map<String, Object> m = new HashMap<>();
                m.put("id", s.getId());
                m.put("name", s.getName());
                m.put("cpf", s.getCpf());
                m.put("course", s.getCourse());
                m.put("coinBalance", s.getCoinBalance());
                return m;
            }).collect(Collectors.toList());
            return ResponseEntity.ok(mapped);
        } catch (Exception e) {
            Map<String, String> err = new HashMap<>();
            err.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(err);
        }
    }

    @PostMapping("/send-coins")
    public ResponseEntity<?> sendCoins(@RequestBody com.moedaestudantil.dto.ProfessorSendCoinsRequest request) {
        try {
            Long professorId = request.getProfessorId();
            Long studentId = request.getStudentId();
            Integer amount = request.getAmount();
            String reason = request.getReason();

            if (studentId == null) throw new RuntimeException("studentId is required");
            if (amount == null || amount <= 0) throw new RuntimeException("Amount must be positive");

            Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));

            Professor professor = null;
            if (professorId != null) {
                professor = professorRepository.findById(professorId)
                    .orElseThrow(() -> new RuntimeException("Professor não encontrado"));
                if (professor.getCoinBalance() < amount) {
                    throw new RuntimeException("Saldo insuficiente do professor");
                }
            }

            // persist transaction
            Transaction tx = new Transaction(professor, student, amount, reason, "PROFESSOR_TO_STUDENT");
            Transaction saved = transactionRepository.save(tx);

            // update student balance
            student.setCoinBalance(student.getCoinBalance() + amount);
            studentRepository.save(student);

            // update professor balance
            if (professor != null) {
                professor.setCoinBalance(professor.getCoinBalance() - amount);
                professorRepository.save(professor);
            }

            com.moedaestudantil.dto.TransactionResponse resp = new com.moedaestudantil.dto.TransactionResponse(
                saved.getId(),
                saved.getProfessor() != null ? saved.getProfessor().getId() : null,
                saved.getStudent().getId(),
                saved.getAmount(),
                saved.getReason(),
                saved.getCreatedAt()
            );

            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> err = new HashMap<>();
            err.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
        }
    }

    @GetMapping("/transactions")
    public ResponseEntity<?> getTransactions(@RequestParam(required = false) Long professorId) {
        try {
            List<Transaction> list;
            if (professorId != null) {
                list = transactionRepository.findByProfessorIdOrderByCreatedAtDesc(professorId);
            } else {
                list = transactionRepository.findByTypeOrderByCreatedAtDesc("PROFESSOR_TO_STUDENT");
            }

            // Map to include student info
            List<Map<String, Object>> mapped = list.stream().map(t -> {
                Map<String, Object> m = new HashMap<>();
                m.put("id", t.getId());
                m.put("professorId", t.getProfessor() != null ? t.getProfessor().getId() : null);
                m.put("studentId", t.getStudent().getId());
                m.put("amount", t.getAmount());
                m.put("reason", t.getReason());
                m.put("created_at", t.getCreatedAt());
                Map<String, Object> s = new HashMap<>();
                s.put("id", t.getStudent().getId());
                s.put("name", t.getStudent().getName());
                s.put("coinBalance", t.getStudent().getCoinBalance());
                m.put("student", s);
                return m;
            }).collect(Collectors.toList());

            return ResponseEntity.ok(mapped);
        } catch (Exception e) {
            Map<String, String> err = new HashMap<>();
            err.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(err);
        }
    }

    /* @GetMapping("/transactions/student/{studentId}")
    public ResponseEntity<?> getTransactionsByStudent(@PathVariable Long studentId) {
        try {
            List<Transaction> list = transactionRepository.findByStudentIdOrderByCreatedAtDesc(studentId);
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            Map<String, String> err = new HashMap<>();
            err.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(err);
        }
    } */
}
