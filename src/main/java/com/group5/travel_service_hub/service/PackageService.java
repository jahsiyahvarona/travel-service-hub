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
     * Saves the uploaded image and returns the image URL.
     *
     * @param imageFile The uploaded image file.
     * @return The image URL.
     * @throws IOException If an error occurs during file saving.
     */
    public String saveImage(MultipartFile imageFile) throws IOException {
        String uploadDir = "src/main/resources/static/package-images/";
        String originalFilename = imageFile.getOriginalFilename();
        String fileExtension = "";

        if (originalFilename != null && originalFilename.contains(".")) {
            fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        String newFilename = UUID.randomUUID().toString() + fileExtension;
        Path imagePath = Paths.get(uploadDir, newFilename);

        // Ensure the directory exists
        Files.createDirectories(imagePath.getParent());

        // Save the file
        Files.copy(imageFile.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);

        // Return the relative path to be used in the src attribute
        return "/package-images/" + newFilename;
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
    public Optional<Package> getPackageByTheId(Long id) {
        return packageRepository.findById(id);
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
     *
     *
     * @param pkg       The Package object with updated information.
     */
    @Transactional
    public void updatePackage(Package pkg) {
        Package existingPackage = packageRepository.findById(pkg.getId())
                .orElseThrow(() -> new IllegalArgumentException("Package not found."));

        existingPackage.setName(pkg.getName());
        existingPackage.setDescription(pkg.getDescription());
        existingPackage.setPrice(pkg.getPrice());

        if (pkg.getImageUrl() != null && !pkg.getImageUrl().isEmpty()) {
            existingPackage.setImageUrl(pkg.getImageUrl());
        }

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
        Package existingPackage = packageRepository.findById(packageId)
                .orElseThrow(() -> new IllegalArgumentException("Package not found."));

        // If user is a PROVIDER, ensure they own the package
        if (user.getRole() == Role.PROVIDER && !existingPackage.getProviderDetails().getId().equals(user.getId())) {
            throw new SecurityException("You do not have permission to delete this package.");
        }

        packageRepository.delete(existingPackage);
    }

    /**
     * Retrieves the count of new bookings for notifications.
     *
     * @param user The authenticated user.
     * @return The number of new bookings.
     */
    public int getNewBookingCount(User user) {
        // Implement logic to retrieve the count of new bookings
        // For example:
        return 3; // Placeholder
    }

    /**
     * Updates the image URL of a Package entity.
     *
     * @param id        The ID of the Package to update.
     * @param imageFile The image file to upload.
     */
    public void updateImage(Long id, MultipartFile imageFile) {
        // Retrieve the Package entity by ID
        Package pkg = packageRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Package not found with ID: " + id));

        // Validate the file
        if (imageFile.isEmpty()) {
            throw new IllegalArgumentException("Uploaded file is empty.");
        }

        // Optionally, validate file type (e.g., only allow JPEG and PNG)
        String contentType = imageFile.getContentType();
        if (contentType == null || !isImage(contentType)) {
            throw new IllegalArgumentException("Only image files (JPEG, PNG) are allowed.");
        }

        // Normalize file name
        String originalFilename = StringUtils.cleanPath(Objects.requireNonNull(imageFile.getOriginalFilename()));

        // Generate a unique file name to prevent collisions
        String fileExtension = getFileExtension(originalFilename);
        String fileName = UUID.randomUUID().toString() + "." + fileExtension;

        // Get the upload directory from application properties
        String uploadDir = env.getProperty("file.upload-dir");

        if (uploadDir == null) {
            throw new IllegalArgumentException("Upload directory not configured.");
        }

        Path uploadPath = Paths.get(uploadDir);

        try {
            // Create directories if they do not exist
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Copy file to the target location (Replacing existing file with the same name)
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Set the imageUrl field (URL)
            String fileUrl = "/uploads/" + fileName;
            pkg.setImageUrl(fileUrl);

            // Save the updated Package entity
            packageRepository.save(pkg);
        } catch (IOException ex) {
            throw new IllegalArgumentException("Could not store file. Please try again!", ex);
        }
    }

    /**
     * Inner class to represent booking notifications.
     */
    public static class BookingNotification {
        private String userName;
        private String packageName;
        private String bookingDate;

        public BookingNotification(String userName, String packageName, String bookingDate) {
            this.userName = userName;
            this.packageName = packageName;
            this.bookingDate = bookingDate;
        }

        public String getUserName() {
            return userName;
        }

        public String getPackageName() {
            return packageName;
        }

        public String getBookingDate() {
            return bookingDate;
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
