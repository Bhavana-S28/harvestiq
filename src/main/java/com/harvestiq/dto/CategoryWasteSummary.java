package com.harvestiq.dto;

public class CategoryWasteSummary {

    private String categoryName;

    private Double totalQuantity;

    public CategoryWasteSummary(String categoryName, Double totalQuantity) {
        this.categoryName = categoryName;
        this.totalQuantity = totalQuantity;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Double getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(Double totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

}
