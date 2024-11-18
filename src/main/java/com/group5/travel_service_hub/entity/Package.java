package com.group5.travel_service_hub.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a travel package offered by a provider.
 */
@Entity
@Table(name = "packages")
public class Package {

    // Unique identifier for the package
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Name of the package (maximum length 50)
    @Column(nullable = false, length = 50)
    private String name;

    // Description of the package (optional, maximum length 1000)
    @Column(length = 1000)
    private String description;

    // Price of the package (cannot be null)
    @Column(nullable = false)
    private Double price;

    // URL for the package's image (optional)
    private String imageUrl;

    // The provider offering the package
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_details_id", nullable = false)
    private User providerDetails;

    // Reviews associated with the package
    @OneToMany(mappedBy = "pkg", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Reviews> Reviews = new HashSet<>();

    // Likes and dislikes associated with the package
    @OneToMany(mappedBy = "pkg", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<LikeDislike> likeDislikes = new HashSet<>();

    // Bookings associated with the package
    @OneToMany(mappedBy = "pkg", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Booking> bookings = new HashSet<>();

    // Constructors

    /**
     * Constructor to initialize a package with all fields.
     *
     * @param id              The ID of the package.
     * @param name            The name of the package.
     * @param description     The description of the package.
     * @param price           The price of the package.
     * @param imageUrl        The URL of the package's image.
     * @param providerDetails The provider offering the package.
     * @param reviews         The reviews associated with the package.
     * @param likeDislikes    The likes and dislikes associated with the package.
     */
    public Package(Long id, String name, String description, Double price, String imageUrl, User providerDetails, Set<Reviews> reviews, Set<LikeDislike> likeDislikes) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.providerDetails = providerDetails;
        Reviews = reviews;
        this.likeDislikes = likeDislikes;
    }

    /**
     * Constructor to initialize a package without an ID.
     *
     * @param name            The name of the package.
     * @param description     The description of the package.
     * @param price           The price of the package.
     * @param imageUrl        The URL of the package's image.
     * @param providerDetails The provider offering the package.
     */
    public Package(String name, String description, Double price, String imageUrl, User providerDetails) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.providerDetails = providerDetails;
    }

    /**
     * Default constructor required by JPA.
     */
    public Package() {
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public User getProviderDetails() {
        return providerDetails;
    }

    public void setProviderDetails(User providerDetails) {
        this.providerDetails = providerDetails;
    }

    public Set<Reviews> getReviews() {
        return Reviews;
    }

    public void setReviews(Set<Reviews> reviews) {
        Reviews = reviews;
    }

    public Set<LikeDislike> getLikeDislikes() {
        return likeDislikes;
    }

    public void setLikeDislikes(Set<LikeDislike> likeDislikes) {
        this.likeDislikes = likeDislikes;
    }

    public Set<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(Set<Booking> bookings) {
        this.bookings = bookings;
    }

}
