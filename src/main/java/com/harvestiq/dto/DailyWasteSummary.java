package com.harvestiq.dto;

import java.time.LocalDate;

public class DailyWasteSummary {

    private LocalDate wasteDate;

    private Double totalQuantity;

    public DailyWasteSummary(LocalDate wasteDate, Double totalQuantity) {
        this.wasteDate = wasteDate;
        this.totalQuantity = totalQuantity;
    }

    public LocalDate getWasteDate() {
        return wasteDate;
    }

    public void setWasteDate(LocalDate wasteDate) {
        this.wasteDate = wasteDate;
    }

    public Double getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(Double totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

}
