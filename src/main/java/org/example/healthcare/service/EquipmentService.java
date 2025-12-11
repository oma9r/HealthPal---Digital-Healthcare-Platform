package org.example.healthcare.service;

import org.example.healthcare.model.Equipment;
import org.example.healthcare.model.NGO;
import org.example.healthcare.repository.EquipmentRepo;
import org.example.healthcare.repository.NGORepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class EquipmentService {
    
    @Autowired
    private EquipmentRepo equipmentRepo;
    
    @Autowired
    private NGORepo ngoRepo;
    
    public List<Equipment> getAllEquipment() {
        return equipmentRepo.findAll();
    }
    
    public List<Equipment> getAvailableEquipment() {
        return equipmentRepo.findByAvailableTrue();
    }
    
    public Optional<Equipment> getEquipmentById(Integer id) {
        return equipmentRepo.findById(id);
    }
    
    public List<Equipment> getEquipmentByNgoId(Integer ngoId) {
        return equipmentRepo.findByNgo_NgoId(ngoId);
    }
    
    public List<Equipment> searchAvailableEquipment(String location, String condition) {
        return equipmentRepo.searchAvailableEquipment(location, condition);
    }
    
    @Transactional
    public Equipment createEquipment(Equipment equipment) {
        if (equipment.getAvailable() == null) {
            equipment.setAvailable(true);
        }
        return equipmentRepo.save(equipment);
    }
    
    @Transactional
    public Equipment updateEquipment(Integer id, Equipment updatedEquipment) {
        return equipmentRepo.findById(id)
            .map(equipment -> {
                if (updatedEquipment.getName() != null) {
                    equipment.setName(updatedEquipment.getName());
                }
                if (updatedEquipment.getDescription() != null) {
                    equipment.setDescription(updatedEquipment.getDescription());
                }
                if (updatedEquipment.getCondition() != null) {
                    equipment.setCondition(updatedEquipment.getCondition());
                }
                if (updatedEquipment.getLocation() != null) {
                    equipment.setLocation(updatedEquipment.getLocation());
                }
                if (updatedEquipment.getAvailable() != null) {
                    equipment.setAvailable(updatedEquipment.getAvailable());
                }
                if (updatedEquipment.getNgo() != null) {
                    equipment.setNgo(updatedEquipment.getNgo());
                }
                return equipmentRepo.save(equipment);
            })
            .orElseThrow(() -> new RuntimeException("Equipment not found with id: " + id));
    }
    
    @Transactional
    public void deleteEquipment(Integer id) {
        equipmentRepo.deleteById(id);
    }
    
    @Transactional
    public Equipment toggleAvailability(Integer id) {
        return equipmentRepo.findById(id)
            .map(equipment -> {
                equipment.setAvailable(!equipment.getAvailable());
                return equipmentRepo.save(equipment);
            })
            .orElseThrow(() -> new RuntimeException("Equipment not found with id: " + id));
    }
}

