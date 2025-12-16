package com.moedaestudantil.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class AdvantageRequest {
    
    @NotBlank(message = "Título é obrigatório")
    private String title;
    
    @NotBlank(message = "Descrição é obrigatória")
    private String description;
    
    private String photoUrl;
    
    @NotNull(message = "Custo em moedas é obrigatório")
    @Positive(message = "Custo deve ser positivo")
    private Integer coinCost;
    
    private Boolean isActive = true;
    
    private Integer maxRedemptions = 1;
    
    // Constructors
    public AdvantageRequest() {}
    
    public AdvantageRequest(String title, String description, Integer coinCost) {
        this.title = title;
        this.description = description;
        this.coinCost = coinCost;
    }
    
    // Getters and Setters
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getPhotoUrl() {
        return photoUrl;
    }
    
    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
    
    public Integer getCoinCost() {
        return coinCost;
    }
    
    public void setCoinCost(Integer coinCost) {
        this.coinCost = coinCost;
    }
    
    public Boolean getIsActive() {
        return isActive;
    }
    
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    
    public Integer getMaxRedemptions() {
        return maxRedemptions;
    }
    
    public void setMaxRedemptions(Integer maxRedemptions) {
        this.maxRedemptions = maxRedemptions;
    }
}
