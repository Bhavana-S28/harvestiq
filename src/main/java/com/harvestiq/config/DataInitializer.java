package com.harvestiq.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.harvestiq.model.Role;
import com.harvestiq.model.User;
import com.harvestiq.model.WasteCategory;
import com.harvestiq.repository.UserRepository;
import com.harvestiq.repository.WasteCategoryRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final WasteCategoryRepository wasteCategoryRepository;

    @Value("${harvestiq.admin.password}")
    private String adminPassword;

    @Value("${harvestiq.staff.password}")
    private String staffPassword;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder,
            WasteCategoryRepository wasteCategoryRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.wasteCategoryRepository = wasteCategoryRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findByEmailIgnoreCase("admin@harvestiq.com").isEmpty()) {
            User admin = new User();
            admin.setEmail("admin@harvestiq.com");
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.setRole(Role.ADMIN);
            userRepository.save(admin);
        }
        if (userRepository.findByEmailIgnoreCase("staff@harvestiq.com").isEmpty()) {
            User staff = new User();
            staff.setEmail("staff@harvestiq.com");
            staff.setPassword(passwordEncoder.encode(staffPassword));
            staff.setRole(Role.STAFF);
            userRepository.save(staff);
        }

        List<String> defaultCategories = List.of("Vegetables", "Fruits", "Dairy", "Bakery", "Beverages");

        for (String categoryName : defaultCategories) {
            if (!wasteCategoryRepository.existsByNameIgnoreCase(categoryName)) {
                WasteCategory category = new WasteCategory();
                category.setName(categoryName);
                wasteCategoryRepository.save(category);
            }
        }
    }
}
