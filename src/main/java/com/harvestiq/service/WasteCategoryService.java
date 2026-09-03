package com.harvestiq.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.harvestiq.model.WasteCategory;
import com.harvestiq.repository.WasteCategoryRepository;

@Service
public class WasteCategoryService {

    private final WasteCategoryRepository wasteCategoryRepository;

    public WasteCategoryService(WasteCategoryRepository wasteCategoryRepository) {
        this.wasteCategoryRepository = wasteCategoryRepository;
    }

    public List<WasteCategory> getAllWasteCategories() {
        return wasteCategoryRepository.findAll();
    }

    public Optional<WasteCategory> getWasteCategoryById(Long id) {
        return wasteCategoryRepository.findById(id);
    }

    public void saveCategory(WasteCategory wasteCategory) {
        wasteCategoryRepository.save(wasteCategory);
    }

    public void deleteCategory(Long id) {
        wasteCategoryRepository.deleteById(id);
    }

    public boolean categoryExists(String name) {
        return wasteCategoryRepository.existsByNameIgnoreCase(name);
    }
}
