package com.group5.travel_service_hub.service;

import com.group5.travel_service_hub.entity.Package;
import com.group5.travel_service_hub.entity.Role;
import com.group5.travel_service_hub.entity.User;
import com.group5.travel_service_hub.repository.PackageRepository;
import com.group5.travel_service_hub.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * Service class for managing Package-related operations.
 */
@Service
public class PackageService {

    @Autowired
    private PackageRepository packageRepository;

    @Autowired
    private UserRepository userRepository;

    // Inject the upload directory from application.properties
    @Autowired
    private Environment env;

    /**
     * Retrieves all packages.
     *
     * @return List of Package objects.
     */
    public List<Package> getAllPackages() {
        return packageRepository.findAll();
    }

    /**
     * Retrieves all packages by a specific provider.
     *
     * @param providerId The ID of the provider.
     * @return List of Package objects.
     */
    public List<Package> getAllPackagesByProvider(Long providerId) {
        return packageRepository.findByProviderDetailsId(providerId);
    }

    /**
     * Searches packages by name across all providers.
     *
     * @param name The name keyword to search for.
     * @return List of Package objects matching the search criteria.
     */
    public List<Package> searchPackagesByName(String name) {
        return packageRepository.findByNameContainingIgnoreCase(name);
    }

    /**
     * Searches packages by name for a specific provider.
     *
     * @param name       The name keyword to search for.
     * @param providerId The ID of the provider.
     * @return List of Package objects matching the search criteria.
     */
    public List<Package> searchPackagesByNameAndProvider(String name, Long providerId) {
        return packageRepository.findByProviderDetailsIdAndNameContainingIgnoreCase(providerId, name);
    }

    /**
     * Creates a new package.
     *
     * @param pkg The Package object to create.
     */
    @Transactional
    public void createPackage(Package pkg) {
        packageRepository.save(pkg);
    }

    /**
     * Retrieves a package by its ID.
     *
     * @param id The ID of the package.
     * @return Optional containing the Package if found, or empty otherwise.
     */
    public Optional<Package> getPackageById(Long id) {
        return packageRepository.findById(id);
    }

    /**
     * Updates an existing package.
     *
     * @param pkg The Package object with updated information.
     */
    @Transactional
    public void updatePackage(Package pkg) {
        // Fetch the package by its ID
        Package existingPackage = packageRepository.findById(pkg.getId())
                .orElseThrow(() -> new IllegalArgumentException("Package not found."));

        // Update package fields
        existingPackage.setName(pkg.getName());
        existingPackage.setDescription(pkg.getDescription());
        existingPackage.setPrice(pkg.getPrice());

        // Update the image URL if provided
        if (pkg.getImageUrl() != null && !pkg.getImageUrl().isEmpty()) {
            existingPackage.setImageUrl(pkg.getImageUrl());
        }

        // Save the updated package
        packageRepository.save(existingPackage);
    }

    /**
     * Deletes a package.
     *
     * @param user      The authenticated user.
     * @param packageId The ID of the package to delete.
     */
    @Transactional
    public void deletePackage(User user, Long packageId) {
        // Fetch the package by ID
        Package existingPackage = packageRepository.findById(packageId)
                .orElseThrow(() -> new IllegalArgumentException("Package not found."));

        // Ensure the user has permission to delete if they're a provider
        if (user.getRole() == Role.PROVIDER && !existingPackage.getProviderDetails().getId().equals(user.getId())) {
            throw new SecurityException("You do not have permission to delete this package.");
        }

        // Delete the package
        packageRepository.delete(existingPackage);
    }

    /**
     * Updates the image URL of a package.
     *
     * @param id        The ID of the package.
     * @param imageFile The image file to upload.
     */
    public void updateImage(Long id, MultipartFile imageFile) {
        // Fetch the package by ID
        Package pkg = packageRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Package not found with ID: " + id));

        // Validate the uploaded file
        if (imageFile.isEmpty()) {
            throw new IllegalArgumentException("Uploaded file is empty.");
        }

        String contentType = imageFile.getContentType();
        if (contentType == null || !isImage(contentType)) {
            throw new IllegalArgumentException("Only image files (JPEG, PNG) are allowed.");
        }

        // Generate a unique filename
        String originalFilename = StringUtils.cleanPath(Objects.requireNonNull(imageFile.getOriginalFilename()));
        String fileExtension = getFileExtension(originalFilename);
        String fileName = UUID.randomUUID() + "." + fileExtension;

        // Fetch the upload directory
        String uploadDir = env.getProperty("file.upload-dir");
        if (uploadDir == null) {
            throw new IllegalArgumentException("Upload directory not configured.");
        }

        Path uploadPath = Paths.get(uploadDir);

        try {
            // Ensure the directory exists
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Save the file
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Set the image URL
            pkg.setImageUrl("/uploads/" + fileName);

            // Save the updated package
            packageRepository.save(pkg);
        } catch (IOException ex) {
            throw new IllegalArgumentException("Could not store file. Please try again!", ex);
        }
    }

    /**
     * Checks if the given content type is an image.
     *
     * @param contentType The content type to check.
     * @return True if it's an image; otherwise, false.
     */
    private boolean isImage(String contentType) {
        return contentType.equalsIgnoreCase("image/jpeg") ||
                contentType.equalsIgnoreCase("image/png") ||
                contentType.equalsIgnoreCase("image/jpg");
    }

    /**
     * Extracts the file extension from the filename.
     *
     * @param filename The filename.
     * @return The file extension.
     */
    private String getFileExtension(String filename) {
        if (filename == null) {
            return "";
        }
        String[] parts = filename.split("\\.");
        return parts.length > 1 ? parts[parts.length - 1] : "";
    }
}
