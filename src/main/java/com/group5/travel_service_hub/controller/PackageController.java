package com.group5.travel_service_hub.controller;

import com.group5.travel_service_hub.entity.City;
import com.group5.travel_service_hub.entity.Package;
import com.group5.travel_service_hub.entity.Role;
import com.group5.travel_service_hub.entity.User;
import com.group5.travel_service_hub.repository.BookingRepository;
import com.group5.travel_service_hub.repository.CityRepository;
import com.group5.travel_service_hub.repository.PackageRepository;
import com.group5.travel_service_hub.service.PackageService;
import com.group5.travel_service_hub.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpSession;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * MVC Controller for managing Package entities within Thymeleaf views.
 */
@Controller
@RequestMapping("/packages")
public class PackageController {

    @Autowired
    private PackageService packageService; // Service for package-related operations

    @Autowired
    private UserService userService; // Service for user-related operations

    @Autowired
    private BookingRepository bookingRepository; // Repository for managing booking data

    @Autowired
    private PackageRepository packageRepository; // Repository for managing package data

    @Autowired
    private CityRepository cityRepository;

    /**
     * Adds a new package entry.
     *
     * @param name                The name of the package.
     * @param description         The description of the package.
     * @param price               The price of the package.
     * @param cityId            The location of the package.
     * @param imageUrl            The uploaded image file (optional).
     * @param session             The HTTP session for the authenticated user.
     * @param redirectAttributes  Redirect attributes for flash messages.
     * @return Redirect to the list of all packages.
     */
    @PostMapping("/create")
    public String addNewPackage(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("price") double price,
            @RequestParam("cityId") Long cityId,
            @RequestParam(value = "imageUrl", required = false) MultipartFile imageUrl,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        User loggedInUser = (User) session.getAttribute("loggedInUser");
        User user = userService.findByUsername(loggedInUser.getUsername());

        // Check user role
        if (user.getRole() != Role.PROVIDER && user.getRole() != Role.SYSADMIN) {
            redirectAttributes.addFlashAttribute("error", "Access denied: Only providers can create packages.");
            return "redirect:/provider/managePackages";
        }

        City city = cityRepository.findById(cityId).orElse(null);
        if (city == null) {
            redirectAttributes.addFlashAttribute("error", "Invalid location selected.");
            return "redirect:/provider/managePackages";
        }

        Package pkg = new Package();
        pkg.setName(name);
        pkg.setDescription(description);
        pkg.setPrice(price);
        pkg.setLocation(city);
        pkg.setProviderDetails(user);

        try {
            // Save the package
            packageService.createPackage(pkg);

            // Handle image upload
            if (imageUrl != null && !imageUrl.isEmpty()) {
                packageService.updateImage(pkg.getId(), imageUrl);
            }

            redirectAttributes.addFlashAttribute("success", "Package created successfully.");
            return "redirect:/provider/managePackages";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to create package: " + e.getMessage());
            return "redirect:/provider/managePackages";
        }
    }


    @PostMapping("/update")
    public String updatePackage(
            @RequestParam("id") Long id,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("price") Double price,
            @RequestParam("cityId") Long cityId,
            @RequestParam(value = "imageUrl", required = false) MultipartFile imageUrl,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        User loggedInUser = (User) session.getAttribute("loggedInUser");
        String username = loggedInUser.getUsername();
        User user = userService.findByUsername(username);

        // Verify role
        if (user.getRole() != Role.PROVIDER && user.getRole() != Role.SYSADMIN) {
            redirectAttributes.addFlashAttribute("error", "Access denied: Only providers can update packages.");
            return "redirect:/provider/managePackages";
        }

        City city = cityRepository.findById(cityId).orElse(null);
        if (city == null) {
            redirectAttributes.addFlashAttribute("error", "Invalid location selected.");
            return "redirect:/provider/managePackages";
        }

        try {
            // Retrieve the existing package
            Optional<Package> optionalPackage = packageService.getPackageById(id);
            if (optionalPackage.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Package not found.");
                return "redirect:/provider/managePackages";
            }

            Package pkg = optionalPackage.get();

            // Update package details
            pkg.setName(name);
            pkg.setDescription(description);
            pkg.setPrice(price);
            pkg.setLocation(city); // Update the location

            if (imageUrl != null && !imageUrl.isEmpty()) {
                // Handle image upload
                packageService.updateImage(pkg.getId(), imageUrl);
            }

            // Save the updated package
            packageService.updatePackage(pkg);

            redirectAttributes.addFlashAttribute("success", "Package updated successfully.");
            return "redirect:/provider/managePackages";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update package: " + e.getMessage());
            return "redirect:/provider/managePackages";
        }
    }



    /**
     * Deletes a package.
     *
     * @param packageId          The ID of the package to delete.
     * @param session            The HTTP session for the authenticated user.
     * @param redirectAttributes Redirect attributes for flash messages.
     * @return Redirect to the Manage Packages page.
     */
    @GetMapping("/delete/{packageId}")
    public String deletePackage(
            @PathVariable Long packageId,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        String username = loggedInUser.getUsername();
        User user = userService.findByUsername(username);

        Package pkg = packageRepository.findById(packageId).orElse(null);
        long bookingCount = bookingRepository.countByPkg(pkg);

        if (bookingCount > 0) {
            redirectAttributes.addFlashAttribute("error", "Cannot delete package: Existing bookings are associated with this package.");
            return "redirect:/provider/managePackages";
        }

        if (user.getRole() != Role.PROVIDER && user.getRole() != Role.SYSADMIN) {
            redirectAttributes.addFlashAttribute("error", "Access denied: Only providers can delete packages.");
            return "redirect:/provider/managePackages";
        }

        try {
            packageService.deletePackage(user, packageId);
            redirectAttributes.addFlashAttribute("success", "Package deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete package: " + e.getMessage());
        }

        return "redirect:/provider/managePackages";
    }

    /**
     * Searches for packages by name.
     *
     * @param keyword   The search keyword.
     * @param principal The authenticated user's details.
     * @param model     The model to attach search results.
     * @return The view for managing packages.
     */
    @GetMapping("/search")
    public String searchPackages(
            @RequestParam String keyword,
            Principal principal,
            Model model) {
        User user = userService.findByUsername(principal.getName());

        if (user.getRole() != Role.PROVIDER && user.getRole() != Role.SYSADMIN) {
            model.addAttribute("error", "Access denied: Only providers can search packages.");
            return "error";
        }

        List<Package> packages = user.getRole() == Role.SYSADMIN
                ? packageService.searchPackagesByName(keyword)
                : packageService.searchPackagesByNameAndProvider(keyword, user.getId());

        model.addAttribute("packages", packages);
        return "frontendCode/ProviderUI/managePackages";
    }

    /**
     * Logs the user out and redirects to the login page.
     *
     * @return Redirect to the login page.
     */
    @GetMapping("/logout")
    public String logoutPage() {
        return "redirect:/login?logout";
    }


}
