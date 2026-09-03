package com.harvestiq.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.harvestiq.dto.CategoryWasteSummary;
import com.harvestiq.dto.DailyWasteSummary;
import com.harvestiq.dto.LocationWasteSummary;
import com.harvestiq.model.WasteRecord;
import com.harvestiq.repository.WasteRepository;
import com.harvestiq.specification.WasteRecordSpecification;

@Service
public class WasteService {

    private final WasteRepository wasteRepository;

    public WasteService(WasteRepository wasteRepository) {
        this.wasteRepository = wasteRepository;
    }

    public void recordWaste(WasteRecord wasteRecord) {
        wasteRepository.save(wasteRecord);
    }

    // Get all waste records
    public List<WasteRecord> getAllWasteRecords() {
        return wasteRepository.findAll();
    }

    public Optional<WasteRecord> getWasteRecordById(Long id) {
        return wasteRepository.findById(id);
    }

    public void deleteWasteRecord(Long id) {
        wasteRepository.deleteById(id);
    }

    // search by foodItem
    public List<WasteRecord> getWasteRecordsByFoodItem(String foodItem) {
        return (wasteRepository.findByFoodItemContainingIgnoreCase(foodItem));
    }

    // search by locationId
    public List<WasteRecord> getWasteRecordsByLocationId(Long locationId) {
        return wasteRepository.findByLocation_Id(locationId);
    }

    // search by locationId and foodItem
    public List<WasteRecord> getWasteRecordsByLocationIdAndFoodItem(Long locationId, String foodItem) {
        return wasteRepository.findByLocation_IdAndFoodItemContainingIgnoreCase(locationId, foodItem);
    }

    // search for all with specification -- used in the controller
    public Page<WasteRecord> searchWasteRecords(String foodItem, Long locationId, Long wasteCategoryId,
            LocalDate fromDate, LocalDate toDate, String sortBy, String direction, int page, int size) {

        Specification<WasteRecord> specification = WasteRecordSpecification.foodItemContains(foodItem)
                .and(WasteRecordSpecification.hasLocation(locationId))
                .and(WasteRecordSpecification.hasWasteCategory(wasteCategoryId))
                .and(WasteRecordSpecification.fromDate(fromDate)).and(WasteRecordSpecification.toDate(toDate));

        List<Integer> allowedPageSizes = List.of(5, 10, 20);

        if (page < 0) {
            page = 0;
        }
        if (!allowedPageSizes.contains(size)) {
            size = 5;
        }
        Pageable pageable = PageRequest.of(page, size);
        if (sortBy == null || sortBy.isBlank()) {
            return wasteRepository.findAll(specification, pageable);
        }
        List<String> allowedSortFields = List.of("foodItem", "quantity", "wasteDate");
        if (!allowedSortFields.contains(sortBy)) {
            return wasteRepository.findAll(specification, pageable);
        }
        Sort.Direction sortDirection = (direction != null && direction.equalsIgnoreCase("desc")) ? Sort.Direction.DESC
                : Sort.Direction.ASC;
        Sort sort = Sort.by(sortDirection, sortBy);
        pageable = PageRequest.of(page, size, sort);
        return wasteRepository.findAll(specification, pageable);
    }

    public boolean hasWasteRecordsForLocation(Long locationId) {
        return wasteRepository.existsByLocationId(locationId);
    }

    public boolean hasWasteRecordsForCategory(Long categoryId) {
        return wasteRepository.existsByWasteCategory_Id(categoryId);
    }

    // Dashboard logic
    public Double getTotalWasteQuantity() {
        return wasteRepository.getTotalWasteQuantity();
    }

    public Long getTotalWasteRecords() {
        return wasteRepository.count();
    }

    public CategoryWasteSummary getTopWasteCategory() {
        List<CategoryWasteSummary> summaries = wasteRepository.getWasteByCategory();
        if (summaries.isEmpty()) {
            return null;
        }
        return summaries.get(0);
    }

    public LocationWasteSummary getTopWasteLocation() {
        List<LocationWasteSummary> locationWasteSummaries = wasteRepository.getWasteByLocation();
        if (locationWasteSummaries.isEmpty()) {
            return null;
        }
        return locationWasteSummaries.get(0);
    }

    public List<CategoryWasteSummary> getWasteByCategory() {
        return wasteRepository.getWasteByCategory();
    }

    public List<LocationWasteSummary> getWasteByLocation() {
        return wasteRepository.getWasteByLocation();
    }

    public Double getWasteThisMonth() {
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.withDayOfMonth(1);
        LocalDate endDate = today.withDayOfMonth(today.lengthOfMonth());
        return wasteRepository.getTotalWasteBetween(startDate, endDate);
    }

    public Double getWasteThisWeek() {
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endDate = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
        return wasteRepository.getTotalWasteBetween(startDate, endDate);
    }

    public List<WasteRecord> getTop5WasteRecords() {
        return wasteRepository.findTop5ByOrderByWasteDateDesc();
    }

    public List<DailyWasteSummary> getWasteByDate() {
        return wasteRepository.getWasteByDate();
    }
}
