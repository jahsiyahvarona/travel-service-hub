package com.group5.travel_service_hub.controller;

import com.group5.travel_service_hub.entity.Package;
import com.group5.travel_service_hub.entity.Role;
import com.group5.travel_service_hub.entity.User;
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
import java.util.List;
import java.util.Optional;

/**
 * MVC Controller for managing Package entities within Thymeleaf views.
 */
@Controller
@RequestMapping("/packages")
public class PackageController {

    @Autowired
    private PackageService packageService;

    @Autowired
    private UserService userService;



    /**
     * Add a new Package entry.
     * Endpoint: POST /packages/create
     *
     * @param name                The new Package object.
     * @param imageUrl     The uploaded image file.
     * @param session        The authenticated user.
     * @param redirectAttributes Redirect attributes for flash messages.
     * @return Redirect to the list of all packages.
     */
    @PostMapping("/create")
    public String addNewPackage(@RequestParam("name") String name,
                                @RequestParam("description") String description,
                                @RequestParam("price") double price,
                                @RequestParam(value = "imageUrl", required = false) MultipartFile imageUrl,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {

        User loggedInUser = (User) session.getAttribute("loggedInUser");
        String username = loggedInUser.getUsername(); // Assuming there's a getUsername() method
        User user = userService.findByUsername(username);


        Package pkg = new Package();

        // Verify the user has the PROVIDER or SYSADMIN role
        if (user.getRole() != Role.PROVIDER && user.getRole() != Role.SYSADMIN) {
            redirectAttributes.addFlashAttribute("error", "Access denied: Only providers can create packages.");
            return "redirect:/provider/managePackages";
        }

            // Set the provider if the user is a PROVIDER
            if (user.getRole() == Role.PROVIDER) {

                pkg.setName(name);
                pkg.setDescription(description);
                pkg.setPrice(price);
                pkg.setProviderDetails(user);

            } else if (user.getRole() == Role.SYSADMIN) {
                // Optionally, allow SYSADMIN to assign a provider
                // This requires additional form fields and logic
                // For simplicity, we'll assume SYSADMIN cannot set provider via this form
            }

        try {

            packageService.createPackage(pkg);

            // Handle profile picture upload
            if (imageUrl != null && !imageUrl.isEmpty()) {
                packageService.updateImage(pkg.getId(), imageUrl);
            }
            redirectAttributes.addFlashAttribute("success", "Package created successfully.");

            return "redirect:/provider/managePackages"; // Adjust as needed


        } catch (Exception e) {
            // Handle registration errors

            return "frontendCode/ProviderUI/managePackages";
        }
    }



    /**
     * Get package data as JSON for editing purposes.
     * Endpoint: GET /providers/packages/{id}/data
     *
     * @param id The ID of the package.
     * @return ResponseEntity containing Package data or 404 status.
     */
    @GetMapping("/{id}/data")
    @ResponseBody
    public ResponseEntity<Package> getPackageData(@PathVariable Long id) {
        Optional<Package> pkgOpt = packageService.getPackageById(id);
        return pkgOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Handle the update of an existing package.
     * Endpoint: POST /providers/packages/update/{packageId}
     *
     *
     *
     *
     *
     * @param redirectAttributes Redirect attributes for flash messages.
     * @return Redirect to the Manage Packages page.
     */
    @PostMapping("/update")
    public String updatePackage(
            @RequestParam("id") Long id,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("price") Double price,
            @RequestParam(value = "imageUrl", required = false) MultipartFile imageUrl,
            HttpSession session,
            RedirectAttributes redirectAttributes ) {



        User loggedInUser = (User) session.getAttribute("loggedInUser");
        String username = loggedInUser.getUsername(); // Assuming there's a getUsername() method
        User user = userService.findByUsername(username);



        // Verify the user has the PROVIDER or SYSADMIN role
        if (user.getRole() != Role.PROVIDER && user.getRole() != Role.SYSADMIN) {
            redirectAttributes.addFlashAttribute("error", "Access denied: Only providers can update packages.");
            return "redirect:/provider/managePackages";
        }

        try {
            Package pkg = new Package();
            pkg.setId(id);
            pkg.setName(name);
            pkg.setDescription(description);
            pkg.setPrice(price);
            // Update the package
            // Process the image upload if a new image is provided
            if (!imageUrl.isEmpty()) {
                packageService.updateImage(pkg.getId(), imageUrl);
            }

            packageService.updatePackage(pkg);

            redirectAttributes.addFlashAttribute("success", "Package updated successfully.");

            return "redirect:/provider/managePackages"; // Adjust as needed


        } catch (SecurityException se) {
            redirectAttributes.addFlashAttribute("error", "You do not have permission to update this package.");
            return "redirect:/provider/managePackages";
        } catch (IllegalArgumentException iae) {
            redirectAttributes.addFlashAttribute("error", "Failed to update package: invalid file type");
            return "redirect:/provider/managePackages";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update package: " + e.getMessage());
            return "redirect:/provider/managePackages";
        }


    }

    /**
     * Handle the deletion of a package.
     * Endpoint: GET /providers/packages/delete/{packageId}
     *
     * @param packageId          The ID of the package to delete.
     * @param session         The authenticated user.
     * @param redirectAttributes Redirect attributes for flash messages.
     * @return Redirect to the Manage Packages page.
     */
    @GetMapping("/delete/{packageId}")
    public String deletePackage(
            @PathVariable Long packageId,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        User loggedInUser = (User) session.getAttribute("loggedInUser");
        String username = loggedInUser.getUsername(); // Assuming there's a getUsername() method
        User user = userService.findByUsername(username);

        // Verify the user has the PROVIDER or SYSADMIN role
        if (user.getRole() != Role.PROVIDER && user.getRole() != Role.SYSADMIN) {
            redirectAttributes.addFlashAttribute("error", "Access denied: Only providers can delete packages.");
            return "redirect:/provider/managePackages";
        }

        try {
            packageService.deletePackage(user, packageId);
            redirectAttributes.addFlashAttribute("success", "Package deleted successfully.");
        } catch (SecurityException se) {
            redirectAttributes.addFlashAttribute("error", "You do not have permission to delete this package.");
        } catch (IllegalArgumentException iae) {
            redirectAttributes.addFlashAttribute("error", "Package not found.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete package: " + e.getMessage());
        }

        return "redirect:/provider/managePackages";
    }

    /**
     * Search for packages by name.
     * Endpoint: GET /providers/packages/search?keyword=xxx
     *
     * @param keyword    The search keyword.
     * @param principal  The authenticated user.
     * @param model      The model to attach attributes.
     * @return The view name "managePackages".
     */
    @GetMapping("/search")
    public String searchPackages(
            @RequestParam String keyword,
            Principal principal,
            Model model) {

        User user = userService.findByUsername(principal.getName());

        // Verify the user has the PROVIDER or SYSADMIN role
        if (user.getRole() != Role.PROVIDER && user.getRole() != Role.SYSADMIN) {
            model.addAttribute("error", "Access denied: Only providers can search packages.");
            return "error"; // Ensure you have an 'error.html' template
        }

        List<Package> packages;

        if (user.getRole() == Role.SYSADMIN) {
            packages = packageService.searchPackagesByName(keyword);
        } else {
            packages = packageService.searchPackagesByNameAndProvider(keyword, user.getId());
        }


        return "frontendCode/ProviderUI/managePackages"; // Ensure this corresponds to 'managePackages.html'
    }

    /**
     * Handle sign out confirmation.
     * Endpoint: GET /logout
     *
     * @return Redirect to the login page.
     */
    @GetMapping("/logout")
    public String logoutPage() {
        return "redirect:/login?logout";
    }
}