package org.example.healthcare.service;

import org.example.healthcare.model.NGO;
import org.example.healthcare.model.User;
import org.example.healthcare.repository.NGORepo;
import org.example.healthcare.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class NGOService {
    
    @Autowired
    private NGORepo ngoRepo;
    
    @Autowired
    private UserRepo userRepo;
    
    public List<NGO> getAllNGOs() {
        return ngoRepo.findAll();
    }
    
    public List<NGO> getVerifiedNGOs() {
        return ngoRepo.findByVerifiedTrue();
    }
    
    public List<NGO> getUnverifiedNGOs() {
        return ngoRepo.findByVerifiedFalse();
    }
    
    public Optional<NGO> getNGOById(Integer id) {
        return ngoRepo.findById(id);
    }
    
    public Optional<NGO> getNGOByUserId(Integer userId) {
        return ngoRepo.findByUser_UserId(userId);
    }
    
    public List<NGO> searchNGOsByName(String name) {
        return ngoRepo.findByNameContainingIgnoreCase(name);
    }
    
    @Transactional
    public NGO createNGO(NGO ngo) {
        if (ngo.getVerified() == null) {
            ngo.setVerified(false);
        }
        return ngoRepo.save(ngo);
    }
    
    @Transactional
    public NGO updateNGO(Integer id, NGO updatedNGO) {
        return ngoRepo.findById(id)
            .map(ngo -> {
                if (updatedNGO.getName() != null) {
                    ngo.setName(updatedNGO.getName());
                }
                if (updatedNGO.getContactInfo() != null) {
                    ngo.setContactInfo(updatedNGO.getContactInfo());
                }
                // Verification status should only be changed by admin
                return ngoRepo.save(ngo);
            })
            .orElseThrow(() -> new RuntimeException("NGO not found with id: " + id));
    }
    
    @Transactional
    public NGO verifyNGO(Integer id) {
        return ngoRepo.findById(id)
            .map(ngo -> {
                ngo.setVerified(true);
                return ngoRepo.save(ngo);
            })
            .orElseThrow(() -> new RuntimeException("NGO not found with id: " + id));
    }
    
    @Transactional
    public NGO unverifyNGO(Integer id) {
        return ngoRepo.findById(id)
            .map(ngo -> {
                ngo.setVerified(false);
                return ngoRepo.save(ngo);
            })
            .orElseThrow(() -> new RuntimeException("NGO not found with id: " + id));
    }
    
    @Transactional
    public void deleteNGO(Integer id) {
        ngoRepo.deleteById(id);
    }
}

