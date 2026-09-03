package com.harvestiq.controller;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.harvestiq.model.RestaurantLocation;
import com.harvestiq.model.WasteCategory;
import com.harvestiq.model.WasteRecord;
import com.harvestiq.service.RestaurantLocationService;
import com.harvestiq.service.WasteCategoryService;
import com.harvestiq.service.WasteService;

import jakarta.validation.Valid;

@Controller
public class HomeController {

    private final WasteService wasteService;

    private final RestaurantLocationService restaurantLocationService;

    private final WasteCategoryService wasteCategoryService;

    public HomeController(WasteService wasteService, RestaurantLocationService restaurantLocationService,
            WasteCategoryService wasteCategoryService) {
        this.wasteService = wasteService;
        this.restaurantLocationService = restaurantLocationService;
        this.wasteCategoryService = wasteCategoryService;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/dashboard";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalWasteQuantity", wasteService.getTotalWasteQuantity());
        model.addAttribute("totalWasteRecords", wasteService.getTotalWasteRecords());
        model.addAttribute("topWasteCategory", wasteService.getTopWasteCategory());
        model.addAttribute("topWasteLocation", wasteService.getTopWasteLocation());
        model.addAttribute("wasteByCategory", wasteService.getWasteByCategory());
        model.addAttribute("wasteByLocation", wasteService.getWasteByLocation());
        model.addAttribute("wasteThisMonth", wasteService.getWasteThisMonth());
        model.addAttribute("wasteThisWeek", wasteService.getWasteThisWeek());
        model.addAttribute("recentWasteRecords", wasteService.getTop5WasteRecords());
        model.addAttribute("wasteByDate", wasteService.getWasteByDate());
        return "dashboard";
    }

    @GetMapping("/record-waste")
    public String recordWaste(Model model) {
        model.addAttribute("wasteRecord", new WasteRecord());
        model.addAttribute("restaurantLocations", restaurantLocationService.getAllRestaurantLocations());
        model.addAttribute("wasteCategories", wasteCategoryService.getAllWasteCategories());
        return "record-waste";
    }

    @PostMapping("/waste")
    public String saveWaste(@Valid WasteRecord wasteRecord, BindingResult bindingResult, Model model,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("restaurantLocations", restaurantLocationService.getAllRestaurantLocations());
            model.addAttribute("wasteCategories", wasteCategoryService.getAllWasteCategories());
            return "record-waste";
        }

        wasteService.recordWaste(wasteRecord);

        redirectAttributes.addFlashAttribute("successMessage", "Waste record submitted successfully!");

        return "redirect:/record-waste";
    }

    @GetMapping("/waste-records")
    public String viewWasteRecords(@RequestParam(required = false) String foodItem,
            @RequestParam(required = false) Long locationId, @RequestParam(required = false) Long wasteCategoryId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String direction,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            Model model) {
        Page<WasteRecord> wasteRecords;

        if (fromDate != null && toDate != null && fromDate.isAfter(toDate)) {
            model.addAttribute("errorMessage", "From date cannot be after To date.");
            wasteRecords = Page.empty();
        } else {
            wasteRecords = wasteService.searchWasteRecords(foodItem, locationId, wasteCategoryId, fromDate, toDate,
                    sortBy, direction, page, size);
        }
        model.addAttribute("wasteRecords", wasteRecords);
        model.addAttribute("restaurantLocations", restaurantLocationService.getAllRestaurantLocations());
        model.addAttribute("wasteCategories", wasteCategoryService.getAllWasteCategories());
        model.addAttribute("locationId", locationId);
        model.addAttribute("foodItem", foodItem);
        model.addAttribute("wasteCategoryId", wasteCategoryId);
        model.addAttribute("fromDate", fromDate);
        model.addAttribute("toDate", toDate);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("direction", direction);
        model.addAttribute("page", page);
        model.addAttribute("size", size);

        return "waste-records";
    }

    @GetMapping("/waste-records/{id}")
    public String viewWasteRecordDetails(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<WasteRecord> wasteRecord = wasteService.getWasteRecordById(id);
        if (wasteRecord.isPresent()) {
            model.addAttribute("wasteRecord", wasteRecord.get());
            return "waste-record-details";
        } else {
            redirectAttributes.addFlashAttribute("error", "WasteRecord not found");
            return "redirect:/waste-records";
        }
    }

    @GetMapping("/waste-records/{id}/edit")
    public String editWasteRecord(@PathVariable Long id, Model model) {
        Optional<WasteRecord> wasteRecord = wasteService.getWasteRecordById(id);
        if (wasteRecord.isEmpty()) {
            return "redirect:/waste-records";
        }
        model.addAttribute("wasteRecord", wasteRecord.get());
        model.addAttribute("restaurantLocations", restaurantLocationService.getAllRestaurantLocations());
        model.addAttribute("wasteCategories", wasteCategoryService.getAllWasteCategories());
        return "record-waste";
    }

    @GetMapping("/waste-records/{id}/delete")
    public String deleteWasteRecord(@PathVariable Long id, Model model) {
        Optional<WasteRecord> wasteRecord = wasteService.getWasteRecordById(id);
        if (wasteRecord.isEmpty()) {
            return "redirect:/waste-records";
        }
        model.addAttribute("wasteRecord", wasteRecord.get());
        return "delete-waste-record";

    }

    @PostMapping("/waste-records/{id}/delete")
    public String confirmDeleteWasteRecord(@PathVariable Long id) {
        wasteService.deleteWasteRecord(id);
        return "redirect:/waste-records";
    }

    @GetMapping("/admin/locations")
    public String manageLocations(Model model) {
        model.addAttribute("restaurantLocations", restaurantLocationService.getAllRestaurantLocations());
        return "manage-locations";
    }

    @GetMapping("/admin/locations/new")
    public String addLocation(Model model) {
        model.addAttribute("restaurantLocation", new RestaurantLocation());
        return "location-form";
    }

    @PostMapping("/admin/locations")
    public String saveLocation(
            @Valid @ModelAttribute RestaurantLocation restaurantLocation,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "location-form";
        }

        boolean duplicate = restaurantLocationService.locationExists(restaurantLocation.getName());

        if (duplicate) {
            if (restaurantLocation.getId() == null) {
                bindingResult.rejectValue(
                        "name",
                        "duplicate",
                        "Location already exists.");
                return "location-form";
            }

            Optional<RestaurantLocation> existingLocation = restaurantLocationService
                    .getRestaurantLocationById(restaurantLocation.getId());

            if (existingLocation.isEmpty()
                    || !existingLocation.get().getName().equalsIgnoreCase(restaurantLocation.getName())) {

                bindingResult.rejectValue(
                        "name",
                        "duplicate",
                        "Location already exists.");
                return "location-form";
            }
        }

        restaurantLocationService.saveLocation(restaurantLocation);
        return "redirect:/admin/locations";
    }

    @GetMapping("/admin/locations/{id}/edit")
    public String editLocation(@PathVariable Long id, Model model) {
        Optional<RestaurantLocation> restaurantLocation = restaurantLocationService.getRestaurantLocationById(id);
        if (restaurantLocation.isEmpty())
            return "redirect:/admin/locations";
        model.addAttribute("restaurantLocation", restaurantLocation.get());
        return "location-form";
    }

    @GetMapping("/admin/locations/{id}/delete")
    public String deleteLocation(@PathVariable Long id, Model model) {
        Optional<RestaurantLocation> restaurantLocation = restaurantLocationService.getRestaurantLocationById(id);
        if (restaurantLocation.isEmpty())
            return "redirect:/admin/locations";
        model.addAttribute("restaurantLocation", restaurantLocation.get());
        return "location-delete";
    }

    @PostMapping("/admin/locations/{id}/delete")
    public String confirmDeleteLocation(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (wasteService.hasWasteRecordsForLocation(id)) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Cannot delete this location because waste records are associated with it.");
            return "redirect:/admin/locations";
        }
        restaurantLocationService.deleteLocation(id);
        return "redirect:/admin/locations";
    }

    @GetMapping("/admin/categories")
    public String manageCategories(Model model) {
        model.addAttribute("wasteCategories", wasteCategoryService.getAllWasteCategories());
        return "manage-categories";
    }

    @GetMapping("/admin/categories/new")
    public String addCategory(Model model) {
        model.addAttribute("wasteCategory", new WasteCategory());
        return "category-form";
    }

    @PostMapping("/admin/categories")
    public String saveCategory(
            @Valid @ModelAttribute WasteCategory wasteCategory,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "category-form";
        }

        boolean duplicate = wasteCategoryService.categoryExists(wasteCategory.getName());

        if (duplicate) {
            if (wasteCategory.getId() == null) {
                bindingResult.rejectValue(
                        "name",
                        "duplicate",
                        "Category already exists.");
                return "category-form";
            }

            Optional<WasteCategory> existingCategory = wasteCategoryService.getWasteCategoryById(wasteCategory.getId());

            if (existingCategory.isEmpty()
                    || !existingCategory.get().getName().equalsIgnoreCase(wasteCategory.getName())) {

                bindingResult.rejectValue(
                        "name",
                        "duplicate",
                        "Category already exists.");
                return "category-form";
            }
        }

        wasteCategoryService.saveCategory(wasteCategory);
        return "redirect:/admin/categories";
    }

    @GetMapping("/admin/categories/{id}/edit")
    public String editCategory(@PathVariable Long id, Model model) {
        Optional<WasteCategory> wasteCategory = wasteCategoryService.getWasteCategoryById(id);
        if (wasteCategory.isEmpty()) {
            return "redirect:/admin/categories";
        }
        model.addAttribute("wasteCategory", wasteCategory.get());
        return "category-form";

    }

    @GetMapping("/admin/categories/{id}/delete")
    public String deleteCategory(@PathVariable Long id, Model model) {
        Optional<WasteCategory> wasteCategory = wasteCategoryService.getWasteCategoryById(id);
        if (wasteCategory.isEmpty()) {
            return "redirect:/admin/categories";
        }
        model.addAttribute("wasteCategory", wasteCategory.get());
        return "category-delete";
    }

    @PostMapping("/admin/categories/{id}/delete")
    public String confirmDeleteCategory(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (wasteService.hasWasteRecordsForCategory(id)) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Cannot delete this category because waste records are associated with it.");
            return "redirect:/admin/categories";
        }
        wasteCategoryService.deleteCategory(id);
        return "redirect:/admin/categories";
    }

}
