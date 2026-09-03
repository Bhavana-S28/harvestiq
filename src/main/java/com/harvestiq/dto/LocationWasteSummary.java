package com.harvestiq.dto;

public class LocationWasteSummary {

    private String locationName;

    private Double totalQuantity;

    public LocationWasteSummary(String locationName, Double totalQuantity) {
        this.locationName = locationName;
        this.totalQuantity = totalQuantity;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public Double getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(Double totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

}
