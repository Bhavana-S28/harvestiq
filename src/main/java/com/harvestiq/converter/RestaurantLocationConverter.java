package com.harvestiq.converter;

import java.util.Optional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.harvestiq.model.RestaurantLocation;
import com.harvestiq.service.RestaurantLocationService;

@Component
public class RestaurantLocationConverter implements Converter<String, RestaurantLocation> {

    private final RestaurantLocationService restaurantLocationService;

    public RestaurantLocationConverter(RestaurantLocationService restaurantLocationService) {
        this.restaurantLocationService = restaurantLocationService;
    }

    @Override
    public RestaurantLocation convert(String source) {

        if (source == null || source.isBlank()) {
            return null;
        }
        Long id = Long.parseLong(source);
        Optional<RestaurantLocation> restaurantLocation = restaurantLocationService.getRestaurantLocationById(id);
        if (restaurantLocation.isPresent()) {
            return restaurantLocation.get();
        }

        throw new IllegalArgumentException("Restaurant location not found for id: " + id);
    }

}
