package com.harvestiq.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.harvestiq.model.WasteCategory;

public interface WasteCategoryRepository extends JpaRepository<WasteCategory, Long> {

    boolean existsByNameIgnoreCase(String name);

}
