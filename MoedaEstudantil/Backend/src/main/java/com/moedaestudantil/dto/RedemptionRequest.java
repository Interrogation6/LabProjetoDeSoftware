package com.moedaestudantil.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class RedemptionRequest {
    
    @NotNull(message = "ID da vantagem é obrigatório")
    private Long advantageId;
    
    @Email(message = "Email do aluno deve ser válido")
    @NotBlank(message = "Email do aluno é obrigatório")
    private String studentEmail;
    
    @NotBlank(message = "Nome do aluno é obrigatório")
    private String studentName;
    
    // Constructors
    public RedemptionRequest() {}
    
    public RedemptionRequest(Long advantageId, String studentEmail, String studentName) {
        this.advantageId = advantageId;
        this.studentEmail = studentEmail;
        this.studentName = studentName;
    }
    
    // Getters and Setters
    public Long getAdvantageId() {
        return advantageId;
    }
    
    public void setAdvantageId(Long advantageId) {
        this.advantageId = advantageId;
    }
    
    public String getStudentEmail() {
        return studentEmail;
    }
    
    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }
    
    public String getStudentName() {
        return studentName;
    }
    
    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
}
