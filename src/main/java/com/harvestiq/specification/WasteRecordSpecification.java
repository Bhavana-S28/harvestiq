package com.harvestiq.specification;

import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;

import com.harvestiq.model.WasteRecord;

public class WasteRecordSpecification {

    public static Specification<WasteRecord> foodItemContains(String foodItem) {

        if (foodItem == null || foodItem.isBlank()) {

            return Specification.unrestricted();
        }
        String normalizedFoodItem = foodItem.trim().toLowerCase();
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("foodItem")),
                    "%" + normalizedFoodItem + "%");
        };

    }

    public static Specification<WasteRecord> hasLocation(Long locationId) {
        if (locationId == null) {
            return Specification.unrestricted();
        }
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("location").get("id"), locationId);
        };
    }

    public static Specification<WasteRecord> hasWasteCategory(Long wasteCategoryId) {
        if (wasteCategoryId == null) {
            return Specification.unrestricted();
        }
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("wasteCategory").get("id"), wasteCategoryId);
        };
    }

    public static Specification<WasteRecord> fromDate(LocalDate fromDate) {
        if (fromDate == null) {
            return Specification.unrestricted();
        }
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.greaterThanOrEqualTo(root.get("wasteDate"), fromDate);
        };
    }

    public static Specification<WasteRecord> toDate(LocalDate toDate) {
        if (toDate == null) {
            return Specification.unrestricted();
        }
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.lessThanOrEqualTo(root.get("wasteDate"), toDate);
        };
    }

}