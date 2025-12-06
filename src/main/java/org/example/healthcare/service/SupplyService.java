package org.example.healthcare.service;

import org.example.healthcare.model.Supply;
import org.example.healthcare.repository.SupplyRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class SupplyService {
    
    @Autowired
    private SupplyRepo supplyRepo;
    
    public List<Supply> getAllSupplies() {
        return supplyRepo.findAll();
    }
    
    public Optional<Supply> getSupplyById(Integer id) {
        return supplyRepo.findById(id);
    }
    
    public List<Supply> getSuppliesByNgoId(Integer ngoId) {
        return supplyRepo.findByNgo_NgoId(ngoId);
    }
    
    public List<Supply> getSuppliesByCategory(String category) {
        return supplyRepo.findByCategory(category);
    }
    
    public List<Supply> getAvailableSuppliesByCategory(String category) {
        return supplyRepo.findAvailableByCategory(category);
    }
    
    public List<Supply> getExpiringSupplies(int daysAhead) {
        LocalDate expiryDate = LocalDate.now().plusDays(daysAhead);
        return supplyRepo.findExpiringSupplies(Date.valueOf(expiryDate));
    }
    
    @Transactional
    public Supply createSupply(Supply supply) {
        if (supply.getQuantity() == null) {
            supply.setQuantity(0);
        }
        return supplyRepo.save(supply);
    }
    
    @Transactional
    public Supply updateSupply(Integer id, Supply updatedSupply) {
        return supplyRepo.findById(id)
            .map(supply -> {
                if (updatedSupply.getName() != null) {
                    supply.setName(updatedSupply.getName());
                }
                if (updatedSupply.getCategory() != null) {
                    supply.setCategory(updatedSupply.getCategory());
                }
                if (updatedSupply.getQuantity() != null) {
                    supply.setQuantity(updatedSupply.getQuantity());
                }
                if (updatedSupply.getExpiryDate() != null) {
                    supply.setExpiryDate(updatedSupply.getExpiryDate());
                }
                if (updatedSupply.getLocation() != null) {
                    supply.setLocation(updatedSupply.getLocation());
                }
                if (updatedSupply.getNgo() != null) {
                    supply.setNgo(updatedSupply.getNgo());
                }
                return supplyRepo.save(supply);
            })
            .orElseThrow(() -> new RuntimeException("Supply not found with id: " + id));
    }
    
    @Transactional
    public Supply updateQuantity(Integer id, Integer quantityChange) {
        return supplyRepo.findById(id)
            .map(supply -> {
                int newQuantity = supply.getQuantity() + quantityChange;
                if (newQuantity < 0) {
                    throw new RuntimeException("Insufficient quantity. Available: " + supply.getQuantity());
                }
                supply.setQuantity(newQuantity);
                return supplyRepo.save(supply);
            })
            .orElseThrow(() -> new RuntimeException("Supply not found with id: " + id));
    }
    
    @Transactional
    public void deleteSupply(Integer id) {
        supplyRepo.deleteById(id);
    }
}

