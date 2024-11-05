package com.group5.travel_service_hub.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "packages")
public class Package {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private Double price;

    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_details_id", nullable = false)
    private User providerDetails;

    @OneToMany(mappedBy = "pkg", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Reviews> Reviews = new HashSet<>();

    @OneToMany(mappedBy = "pkg", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<LikeDislike> likeDislikes = new HashSet<>();

    // Constructors


    public Package(Long id, String name, String description, Double price, String imageUrl, User providerDetails, Set<com.group5.travel_service_hub.entity.Reviews> reviews, Set<LikeDislike> likeDislikes) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.providerDetails = providerDetails;
        Reviews = reviews;
        this.likeDislikes = likeDislikes;
    }

    public Package(String name, String description, Double price, String imageUrl, User providerDetails) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.providerDetails = providerDetails;

    }

    public Package() {}

    // Getters and Setters


    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

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

    public User getProviderDetails() {
        return providerDetails;
    }

    public void setProviderDetails(User providerDetails) {
        this.providerDetails = providerDetails;
    }



    public Set<LikeDislike> getLikeDislikes() {
        return likeDislikes;
    }

    public void setLikeDislikes(Set<LikeDislike> likeDislikes) {
        this.likeDislikes = likeDislikes;
    }



    // Methods to manage likeDislikes
    public void addLikeDislike(LikeDislike likeDislike) {
        likeDislikes.add(likeDislike);
        likeDislike.setPkg(this);
    }

    public void removeLikeDislike(LikeDislike likeDislike) {
        likeDislikes.remove(likeDislike);
        likeDislike.setPkg(null);
    }
}