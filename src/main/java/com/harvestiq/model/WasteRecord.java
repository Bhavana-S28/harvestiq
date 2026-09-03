package com.harvestiq.model;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "waste_record")
public class WasteRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Food item is required.")
    @Column(name = "food_item")
    private String foodItem;

    @NotNull(message = "Please enter a quantity.")
    @Positive(message = "Quantity must be greater than 0.")
    @Column(name = "quantity")
    private Double quantity;

    @ManyToOne
    @JoinColumn(name = "waste_category_id")
    @NotNull(message = "Please select a waste category.")
    private WasteCategory wasteCategory;

    @Column(name = "waste_reason")
    private String wasteReason;

    @PastOrPresent(message = "Waste date cannot be in the future.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @NotNull(message = "Please select a waste date.")
    @Column(name = "waste_date")
    private LocalDate wasteDate;

    @ManyToOne
    @JoinColumn(name = "location_id")
    @NotNull(message = "Please select a location.")
    private RestaurantLocation location;

    public WasteRecord() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFoodItem() {
        return foodItem;
    }

    public void setFoodItem(String foodItem) {
        this.foodItem = foodItem;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public WasteCategory getWasteCategory() {
        return wasteCategory;
    }

    public void setWasteCategory(WasteCategory wasteCategory) {
        this.wasteCategory = wasteCategory;
    }

    public String getWasteReason() {
        return wasteReason;
    }

    public void setWasteReason(String wasteReason) {
        this.wasteReason = wasteReason;
    }

    public LocalDate getWasteDate() {
        return wasteDate;
    }

    public void setWasteDate(LocalDate wasteDate) {
        this.wasteDate = wasteDate;
    }

    public RestaurantLocation getLocation() {
        return location;
    }

    public void setLocation(RestaurantLocation location) {
        this.location = location;
    }

}
