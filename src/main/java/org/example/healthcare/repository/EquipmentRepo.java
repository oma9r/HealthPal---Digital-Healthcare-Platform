package org.example.healthcare.repository;

import org.example.healthcare.model.Equipment;
import org.example.healthcare.model.NGO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipmentRepo extends JpaRepository<Equipment, Integer> {
    List<Equipment> findByAvailableTrue();
    List<Equipment> findByAvailableFalse();
    List<Equipment> findByNgo(NGO ngo);
    List<Equipment> findByNgo_NgoId(Integer ngoId);
    List<Equipment> findByLocation(String location);
    List<Equipment> findByCondition(String condition);
    
    @Query("SELECT e FROM Equipment e WHERE e.available = true AND " +
           "(:location IS NULL OR LOWER(e.location) LIKE LOWER(CONCAT('%', :location, '%'))) AND " +
           "(:condition IS NULL OR LOWER(e.condition) LIKE LOWER(CONCAT('%', :condition, '%')))")
    List<Equipment> searchAvailableEquipment(@Param("location") String location, @Param("condition") String condition);
}

