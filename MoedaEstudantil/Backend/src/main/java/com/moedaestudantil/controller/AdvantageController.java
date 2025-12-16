package com.moedaestudantil.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moedaestudantil.dto.AdvantageRequest;
import com.moedaestudantil.dto.AdvantageResponse;
import com.moedaestudantil.dto.RedemptionRequest;
import com.moedaestudantil.dto.RedemptionResponse;
import com.moedaestudantil.model.Advantage;
import com.moedaestudantil.model.Redemption;
import com.moedaestudantil.service.AdvantageService;
import com.moedaestudantil.repository.RedemptionRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/advantages")
public class AdvantageController {
    
    @Autowired
    private AdvantageService advantageService;

    @Autowired
    private RedemptionRepository redemptionRepository;
    
    @PostMapping("/company/{companyId}")
    public ResponseEntity<?> createAdvantage(@PathVariable Long companyId,
                                           @RequestBody AdvantageRequest request) {
        try {
            System.out.println("Creating advantage for company: " + companyId);
            System.out.println("Request: " + request);
            
            Advantage advantage = advantageService.createAdvantage(companyId, request);
            System.out.println("Advantage created: " + advantage.getId());
            
            // Retornar DTO sem referências circulares
            AdvantageResponse response = new AdvantageResponse(
                advantage.getId(),
                advantage.getTitle(),
                advantage.getDescription(),
                advantage.getPhotoUrl(),
                advantage.getCoinCost(),
                advantage.getIsActive(),
                advantage.getMaxRedemptions(),
                advantage.getCurrentRedemptions(),
                advantage.getCreatedAt()
            );
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.out.println("Error creating advantage: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<AdvantageResponse>> getAdvantagesByCompany(@PathVariable Long companyId) {
        List<Advantage> advantages = advantageService.getAdvantagesByCompany(companyId);
        List<AdvantageResponse> response = advantages.stream()
            .map(adv -> new AdvantageResponse(
                adv.getId(),
                adv.getTitle(),
                adv.getDescription(),
                adv.getPhotoUrl(),
                adv.getCoinCost(),
                adv.getIsActive(),
                adv.getMaxRedemptions(),
                adv.getCurrentRedemptions(),
                adv.getCreatedAt()
            ))
            .toList();
        return ResponseEntity.ok(response);
    }
    
    @GetMapping
    public ResponseEntity<List<AdvantageResponse>> getAvailableAdvantages() {
        List<Advantage> advantages = advantageService.getAvailableAdvantages();
        List<AdvantageResponse> response = advantages.stream()
            .map(adv -> new AdvantageResponse(
                adv.getId(),
                adv.getTitle(),
                adv.getDescription(),
                adv.getPhotoUrl(),
                adv.getCoinCost(),
                adv.getIsActive(),
                adv.getMaxRedemptions(),
                adv.getCurrentRedemptions(),
                adv.getCreatedAt()
            ))
            .toList();
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/affordable/{maxCost}")
    public ResponseEntity<List<AdvantageResponse>> getAffordableAdvantages(@PathVariable Integer maxCost) {
        List<Advantage> advantages = advantageService.getAffordableAdvantages(maxCost);
        List<AdvantageResponse> response = advantages.stream()
            .map(adv -> new AdvantageResponse(
                adv.getId(),
                adv.getTitle(),
                adv.getDescription(),
                adv.getPhotoUrl(),
                adv.getCoinCost(),
                adv.getIsActive(),
                adv.getMaxRedemptions(),
                adv.getCurrentRedemptions(),
                adv.getCreatedAt()
            ))
            .toList();
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<AdvantageResponse> getAdvantageById(@PathVariable Long id) {
        return advantageService.getAdvantageById(id)
                .map(adv -> new AdvantageResponse(
                    adv.getId(),
                    adv.getTitle(),
                    adv.getDescription(),
                    adv.getPhotoUrl(),
                    adv.getCoinCost(),
                    adv.getIsActive(),
                    adv.getMaxRedemptions(),
                    adv.getCurrentRedemptions(),
                    adv.getCreatedAt()
                ))
                .map(resp -> ResponseEntity.ok(resp))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateAdvantage(@PathVariable Long id,
                                           @Valid @RequestBody AdvantageRequest request) {
        try {
            System.out.println("Updating advantage with ID: " + id);
            System.out.println("Request: " + request);
            
            Advantage advantage = advantageService.updateAdvantage(id, request);
            System.out.println("Advantage updated successfully");
            
            // Retornar DTO sem referências circulares
            AdvantageResponse response = new AdvantageResponse(
                advantage.getId(),
                advantage.getTitle(),
                advantage.getDescription(),
                advantage.getPhotoUrl(),
                advantage.getCoinCost(),
                advantage.getIsActive(),
                advantage.getMaxRedemptions(),
                advantage.getCurrentRedemptions(),
                advantage.getCreatedAt()
            );
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.out.println("Error updating advantage: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAdvantage(@PathVariable Long id) {
        try {
            advantageService.deleteAdvantage(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @PostMapping("/redeem")
    public ResponseEntity<?> redeemAdvantage(@Valid @RequestBody RedemptionRequest request) {
        try {
            Redemption redemption = advantageService.redeemAdvantage(request);
            RedemptionResponse response = new RedemptionResponse(
                redemption.getId(),
                redemption.getAdvantage().getId(),
                redemption.getAdvantage().getTitle(),
                redemption.getAdvantage().getDescription(),
                redemption.getAdvantage().getCoinCost(),
                redemption.getCouponCode(),
                redemption.getStudent() != null ? redemption.getStudent().getId() : null,
                redemption.getStudentEmail(),
                redemption.getStudentName(),
                redemption.getCreatedAt()
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
    
    @GetMapping("/redemptions/company/{companyId}")
    public ResponseEntity<List<Redemption>> getRedemptionsByCompany(@PathVariable Long companyId) {
        List<Redemption> redemptions = advantageService.getRedemptionsByCompany(companyId);
        return ResponseEntity.ok(redemptions);
    }
    
    @GetMapping("/redemptions/coupon/{couponCode}")
    public ResponseEntity<Redemption> getRedemptionByCouponCode(@PathVariable String couponCode) {
        return advantageService.getRedemptionByCouponCode(couponCode)
                .map(redemption -> ResponseEntity.ok(redemption))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/redemptions/student/{studentId}")
    public ResponseEntity<List<Redemption>> getRedemptionsByStudent(@PathVariable Long studentId) {
        List<Redemption> redemptions = redemptionRepository.findByStudent_IdOrderByCreatedAtDesc(studentId);
        return ResponseEntity.ok(redemptions);
    }
}
