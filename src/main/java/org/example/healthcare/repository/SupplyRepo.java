package org.example.healthcare.repository;

import org.example.healthcare.model.NGO;
import org.example.healthcare.model.Supply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface SupplyRepo extends JpaRepository<Supply, Integer> {
    List<Supply> findByNgo(NGO ngo);
    List<Supply> findByNgo_NgoId(Integer ngoId);
    List<Supply> findByCategory(String category);
    List<Supply> findByExpiryDateBefore(Date date);
    List<Supply> findByExpiryDateBetween(Date startDate, Date endDate);
    List<Supply> findByQuantityGreaterThan(Integer quantity);
    
    @Query("SELECT s FROM Supply s WHERE s.expiryDate <= :date AND s.quantity > 0 ORDER BY s.expiryDate ASC")
    List<Supply> findExpiringSupplies(@Param("date") Date date);
    
    @Query("SELECT s FROM Supply s WHERE s.category = :category AND s.quantity > 0")
    List<Supply> findAvailableByCategory(@Param("category") String category);
}

