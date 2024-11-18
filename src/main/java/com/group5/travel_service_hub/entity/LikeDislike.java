package com.group5.travel_service_hub.entity;

import jakarta.persistence.*;

/**
 * Represents a "like" or "dislike" reaction on a package by a user.
 */
@Entity
@Table(name = "like_dislikes")
public class LikeDislike {

    // Unique identifier for the LikeDislike entity
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Indicates whether the reaction is a like (true) or dislike (false)
    @Column(nullable = false)
    private boolean isLike;

    // The package associated with the like/dislike
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "package_id", nullable = false)
    private Package pkg;

    // The user who gave the like/dislike
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // The provider of the package that received the like/dislike
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id", nullable = false)
    private User provider;

    // Constructors

    /**
     * Default constructor required by JPA.
     */
    public LikeDislike() {}

    /**
     * Constructor to create a new LikeDislike entity without an ID.
     *
     * @param like     Indicates if the reaction is a like (true) or dislike (false).
     * @param pkg      The package being liked/disliked.
     * @param user     The user who liked/disliked the package.
     * @param provider The provider of the package.
     */
    public LikeDislike(boolean like, Package pkg, User user, User provider) {
        this.isLike = like;
        this.pkg = pkg;
        this.user = user;
        this.provider = provider;
    }

    /**
     * Constructor to create a LikeDislike entity with an ID.
     *
     * @param id       The unique ID of the reaction.
     * @param like     Indicates if the reaction is a like (true) or dislike (false).
     * @param pkg      The package being liked/disliked.
     * @param user     The user who liked/disliked the package.
     * @param provider The provider of the package.
     */
    public LikeDislike(Long id, boolean like, Package pkg, User user, User provider) {
        this.id = id;
        this.isLike = like;
        this.pkg = pkg;
        this.user = user;
        this.provider = provider;
    }

    // Getters and Setters

    /**
     * Gets the unique ID of the LikeDislike entity.
     *
     * @return The ID of the reaction.
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique ID of the LikeDislike entity.
     *
     * @param id The ID to set.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Checks if the reaction is a like.
     *
     * @return True if it's a like, false if it's a dislike.
     */
    public boolean isLike() {
        return isLike;
    }

    /**
     * Sets the reaction type (like or dislike).
     *
     * @param like True for like, false for dislike.
     */
    public void setLike(boolean like) {
        this.isLike = like;
    }

    /**
     * Gets the package associated with the reaction.
     *
     * @return The package entity.
     */
    public Package getPkg() {
        return pkg;
    }

    /**
     * Sets the package associated with the reaction.
     *
     * @param pkg The package entity to set.
     */
    public void setPkg(Package pkg) {
        this.pkg = pkg;
    }

    /**
     * Gets the user who gave the reaction.
     *
     * @return The user entity.
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets the user who gave the reaction.
     *
     * @param user The user entity to set.
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Gets the provider of the package that received the reaction.
     *
     * @return The provider entity.
     */
    public User getProvider() {
        return provider;
    }

    /**
     * Sets the provider of the package that received the reaction.
     *
     * @param provider The provider entity to set.
     */
    public void setProvider(User provider) {
        this.provider = provider;
    }
}
