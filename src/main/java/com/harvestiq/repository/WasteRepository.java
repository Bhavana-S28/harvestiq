package com.harvestiq.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.harvestiq.dto.CategoryWasteSummary;
import com.harvestiq.dto.DailyWasteSummary;
import com.harvestiq.dto.LocationWasteSummary;
import com.harvestiq.model.WasteRecord;

public interface WasteRepository extends JpaRepository<WasteRecord, Long>, JpaSpecificationExecutor<WasteRecord> {

    List<WasteRecord> findByLocation_Id(Long locationId);

    List<WasteRecord> findByLocation_Name(String locationName);

    List<WasteRecord> findByFoodItemContainingIgnoreCase(String foodItem);

    List<WasteRecord> findByLocation_IdAndFoodItemContainingIgnoreCase(Long locationId, String foodItem);

    boolean existsByLocationId(Long locationId);

    boolean existsByWasteCategory_Id(Long categoryId);

    @Query("SELECT COALESCE(SUM(w.quantity), 0.0) FROM WasteRecord w")
    Double getTotalWasteQuantity();

    @Query("SELECT NEW  com.harvestiq.dto.CategoryWasteSummary(w.wasteCategory.name, SUM(w.quantity)) FROM WasteRecord w GROUP BY w.wasteCategory.name ORDER BY SUM(w.quantity) DESC")
    List<CategoryWasteSummary> getWasteByCategory();

    @Query("SELECT NEW com.harvestiq.dto.LocationWasteSummary(w.location.name, SUM(w.quantity)) FROM WasteRecord w GROUP BY w.location.name ORDER BY SUM(w.quantity) DESC")
    List<LocationWasteSummary> getWasteByLocation();

    @Query("SELECT COALESCE(SUM(w.quantity), 0.0) FROM WasteRecord w WHERE w.wasteDate BETWEEN :startDate AND :endDate")
    Double getTotalWasteBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    List<WasteRecord> findTop5ByOrderByWasteDateDesc();

    @Query("SELECT NEW com.harvestiq.dto.DailyWasteSummary(w.wasteDate, SUM(w.quantity)) FROM WasteRecord w GROUP BY w.wasteDate ORDER BY w.wasteDate ASC")
    List<DailyWasteSummary> getWasteByDate();
}
