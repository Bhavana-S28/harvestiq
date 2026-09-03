package com.harvestiq.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.harvestiq.model.RestaurantLocation;

public interface RestaurantLocationRepository extends JpaRepository<RestaurantLocation, Long> {
    
    boolean existsByNameIgnoreCase(String name);
    
}
