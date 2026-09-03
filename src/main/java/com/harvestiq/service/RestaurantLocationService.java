package com.harvestiq.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.harvestiq.model.RestaurantLocation;
import com.harvestiq.repository.RestaurantLocationRepository;

@Service
public class RestaurantLocationService {

    private final RestaurantLocationRepository restaurantLocationRepository;

    public RestaurantLocationService(RestaurantLocationRepository restaurantLocationRepository) {
        this.restaurantLocationRepository = restaurantLocationRepository;
    }

    public List<RestaurantLocation> getAllRestaurantLocations() {
        return restaurantLocationRepository.findAll();
    }

    public Optional<RestaurantLocation> getRestaurantLocationById(Long id) {
        return restaurantLocationRepository.findById(id);
    }

    public void saveLocation(RestaurantLocation restaurantLocation) {
        restaurantLocationRepository.save(restaurantLocation);
    }

    public void deleteLocation(Long id) {
        restaurantLocationRepository.deleteById(id);
    }

    public boolean locationExists(String name) {
        return restaurantLocationRepository.existsByNameIgnoreCase(name);
    }
}
