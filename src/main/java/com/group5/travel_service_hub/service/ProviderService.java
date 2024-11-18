package com.group5.travel_service_hub.service;

import com.group5.travel_service_hub.entity.*;
import com.group5.travel_service_hub.entity.Package;
import com.group5.travel_service_hub.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;

/**
 * Service class for managing provider-specific operations.
 */
@Service
public class ProviderService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PackageRepository packageRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private ReviewRepository commentRepository;

    @Autowired
    private UserService userService; // Delegates user-related operations

    /**
     * Saves the uploaded image and returns the image URL.
     *
     * @param imageFile The uploaded image file.
     * @return The image URL.
     * @throws IOException If an error occurs during file saving.
     */
    public String saveImage(MultipartFile imageFile) throws IOException {
        String uploadDir = "package-images/"; // Directory to save images
        String originalFilename = imageFile.getOriginalFilename();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFilename = UUID.randomUUID() + fileExtension;
        Path imagePath = Paths.get(uploadDir, newFilename);

        // Ensure the directory exists
        Files.createDirectories(imagePath.getParent());

        // Save the file
        Files.copy(imageFile.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);

        // Return the relative path or URL to store in the database
        return "/package-images/" + newFilename;
    }

    /**
     * Creates a new travel package for the provider.
     *
     * @param userId The ID of the provider creating the package.
     * @param pkg    The Package entity containing package details.
     */
    @Transactional
    public void createPackage(Long userId, Package pkg) {
        User provider = getProviderUser(userId);
        pkg.setProviderDetails(provider);
        packageRepository.save(pkg);
    }

    /**
     * Updates an existing travel package.
     *
     * @param userId    The ID of the provider updating the package.
     * @param packageId The ID of the package to update.
     * @param pkg       The Package entity containing updated package details.
     */
    @Transactional
    public void updatePackage(Long userId, Long packageId, Package pkg) {
        User provider = getProviderUser(userId);

        Package existingPackage = packageRepository.findById(packageId)
                .orElseThrow(() -> new IllegalArgumentException("Package not found."));

        if (!existingPackage.getProviderDetails().getId().equals(provider.getId())) {
            throw new SecurityException("Unauthorized to update this package.");
        }

        existingPackage.setName(pkg.getName());
        existingPackage.setDescription(pkg.getDescription());
        existingPackage.setPrice(pkg.getPrice());
        if (pkg.getImageUrl() != null) {
            existingPackage.setImageUrl(pkg.getImageUrl());
        }

        packageRepository.save(existingPackage);
    }

    /**
     * Retrieves a travel package by its ID.
     *
     * @param packageId The ID of the package.
     * @return The Package object.
     * @throws IllegalArgumentException If the package is not found.
     */
    public Package getPackageById(Long packageId) {
        return packageRepository.findById(packageId)
                .orElseThrow(() -> new IllegalArgumentException("Package not found."));
    }

    /**
     * Ensures the given user ID belongs to a provider and retrieves the user.
     *
     * @param userId The ID of the user.
     * @return User object.
     * @throws IllegalArgumentException If the user is not found or not a provider.
     */
    private User getProviderUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));
        if (user.getRole() != Role.PROVIDER) {
            throw new IllegalArgumentException("User is not a provider.");
        }
        return user;
    }

    /**
     * Retrieves all travel packages created by the provider.
     *
     * @param userId The ID of the provider.
     * @return List of Package objects.
     */
    public List<Package> getAllPackagesByProvider(Long userId) {
        User provider = getProviderUser(userId);
        return packageRepository.findByProviderDetailsId(provider.getId());
    }
}
